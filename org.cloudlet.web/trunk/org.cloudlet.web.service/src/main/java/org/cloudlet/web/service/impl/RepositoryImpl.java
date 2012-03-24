package org.cloudlet.web.service.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;

import org.cloudlet.web.service.Containment;
import org.cloudlet.web.service.Content;
import org.cloudlet.web.service.Ignored;
import org.cloudlet.web.service.Repository;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.persistence.Id;

public class RepositoryImpl implements Repository {

  private static EcoreFactory eCoreFactory = EcoreFactory.eINSTANCE;

  public static String getNsPrefix(final String pkgName) {
    String prefix = pkgName.substring(pkgName.lastIndexOf(".") + 1);
    return prefix;
  }

  public static String getNsURI(final String pkgName) {
    StringTokenizer st = new StringTokenizer(pkgName, ".");
    StringBuilder builder = new StringBuilder("http://");
    while (st.hasMoreTokens()) {
      builder.append("/").append(st.nextToken());
    }
    builder.append(".ecore");
    return builder.toString();
  }

  public static String getPrefix(final String pkgName) {
    String prefix = pkgName.substring(pkgName.lastIndexOf(".") + 1);
    return prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
  }

  private final ResourceSet resourceSet;

  private Resource data;

  private final Injector injector;

  EPackage.Registry registry;

  @Inject
  public RepositoryImpl(final ResourceSet resourceSet, final Injector injector) {
    this.resourceSet = resourceSet;
    this.injector = injector;
    Map<String, Object> factoryMap =
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
    factoryMap.put("*", new XMIResourceFactoryImpl());
    factoryMap.put("ecore", new EcoreResourceFactoryImpl());

    registry = resourceSet.getPackageRegistry();
    int s = registry.entrySet().size();
    registry.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
  }

  @Override
  public <T> T create(final Class<T> clazz) {
    EClass eClass = getEClass(clazz);
    if (eClass != null) {
      EObject eObject = eClass.getEPackage().getEFactoryInstance().create(eClass);

      Content entity = null;
      if (eObject instanceof Content) {
        entity = (Content) eObject;
      } else {
        // Class<?> instanceClass = ClassUtil.getInstanceClass(eObject.eClass());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class<?>[] interfaces =
            new Class[] {clazz, InternalEObject.class, EObject.class, Content.class};
        ContentImpl Impl = new ContentImpl();
        Impl.setEObject(eObject);
        entity = (Content) Proxy.newProxyInstance(cl, interfaces, Impl);
      }
      return (T) entity;
    }
    return injector.getInstance(clazz);
  }

  @Override
  public Content find(final String uri) {
    EObject eObject = getData().getEObject(uri);
    if (eObject == null) {
      return null;
    }

    if (eObject instanceof Content) {
      return (Content) eObject;
    }

    // TODO cast to subclass
    ContentImpl entity = new ContentImpl();
    entity.setEObject(eObject);
    return entity;
  }

  @Override
  public <T> T findById(final Class<T> clazz, final Object id) {
    System.out.println("debug");
    return null;
  }

  @Override
  public Content findRoot() {
    return find("/");
  }

  public Resource getData() {
    if (data == null) {
      File file = new File("data/data.xml");
      String absPath = file.getAbsolutePath();

      URI uri = URI.createFileURI(absPath);

      if (file.exists()) {
        try {
          data = resourceSet.getResource(uri, true);
        } catch (Exception e) {
          file.delete();
        }
      }

      if (data == null) {
        File folder = file.getParentFile();
        if (!folder.exists()) {
          folder.mkdir();
        }
        data = resourceSet.createResource(uri);
      }
    }
    return data;
  }

  public EClass getEClass(final Class<?> clazz) {
    System.out.println("find: " + clazz);
    EPackage ePackage = getEPackage(clazz);
    String classifierName = ClassUtil.getClassifierName(clazz);
    EClass eClass = (EClass) ePackage.getEClassifier(classifierName);
    if (eClass != null) {
      return eClass;
    }

    eClass = eCoreFactory.createEClass();

    eClass.setName(classifierName);
    // do not set instance class for dynamic model;
    // eClass.setInstanceClass(cls);
    ePackage.getEClassifiers().add(eClass);

    parseAnnotations(eClass, clazz);

    for (TypeVariable tv : clazz.getTypeParameters()) {

      ETypeParameter p = eCoreFactory.createETypeParameter();
      p.setName(tv.getName());
      for (Type gt : tv.getBounds()) {
        Class<?> gc = (Class<?>) gt;
        EGenericType eGt = eCoreFactory.createEGenericType();
        eGt.setEClassifier(getEClass(gc));
        p.getEBounds().add(eGt);
      }
      eClass.getETypeParameters().add(p);
    }

    if (clazz.isInterface()) {
      for (Type genericType : clazz.getGenericInterfaces()) {
        if (genericType instanceof Class) {
          eClass.getESuperTypes().add(getEClass((Class) genericType));
        }
      }
    }

    for (Method m : clazz.getDeclaredMethods()) {

      if (m.getAnnotation(Ignored.class) != null) {
        continue;
      }

      Class<?> returnType = m.getReturnType();
      Type genericeType = m.getGenericReturnType();
      int upperBound;
      EClassifier eType = null;
      EGenericType eGenericType = eCoreFactory.createEGenericType();
      if (List.class.equals(returnType)) {
        upperBound = ETypedElement.UNBOUNDED_MULTIPLICITY;
        if (genericeType instanceof ParameterizedType) {
          ParameterizedType pt = (ParameterizedType) m.getGenericReturnType();
          for (Type tt : pt.getActualTypeArguments()) {
            if (tt instanceof TypeVariable) {
              TypeVariable tv = (TypeVariable) tt;
              for (ETypeParameter tp : eClass.getETypeParameters()) {
                if (tp.getName().equals(tv.getName())) {
                  eGenericType.setETypeParameter(tp);
                  break;
                }
              }
            } else if (tt instanceof Class) {
              eType = getEClassifier((Class) tt);
            } else if (tt instanceof WildcardType) {
              System.err.println("WildcardType is not implemented");
            } else if (tt instanceof GenericArrayType) {
              System.err.println("GenericArrayType is not implemented");
            }
          }
        }
      } else {
        upperBound = 1;
        eType = getEClassifier(returnType);
        if (genericeType instanceof ParameterizedType) {

          EGenericType eGenericTypeArg = eCoreFactory.createEGenericType();
          eGenericType.getETypeArguments().add(eGenericTypeArg);

          ParameterizedType pt = (ParameterizedType) m.getGenericReturnType();
          for (Type tt : pt.getActualTypeArguments()) {
            if (tt instanceof TypeVariable) {
              TypeVariable tv = (TypeVariable) tt;
              // TODO
            } else if (tt instanceof Class) {
              EClassifier eArgType = getEClassifier((Class) tt);
              eGenericTypeArg.setEClassifier(eArgType);
            } else if (tt instanceof WildcardType) {
              System.err.println("WildcardType is not implemented");
            } else if (tt instanceof GenericArrayType) {
              System.err.println("GenericArrayType is not implemented");
            }
          }
        }
      }

      if (eType != null) {
        eGenericType.setEClassifier(eType);
      }

      Feature feature = Feature.parse(m);

      if (feature != null) {
        if (feature.isGetter()) {
          if (eType instanceof EDataType) {
            EAttribute eAttribute = eCoreFactory.createEAttribute();
            eAttribute.setName(feature.getName());
            eAttribute.setUpperBound(upperBound);
            if (m.getAnnotation(Id.class) != null) {
              eAttribute.setID(true);
            }

            eClass.getEStructuralFeatures().add(eAttribute);
            eAttribute.setEType(eType);
            eAttribute.setEGenericType(eGenericType);
          } else {

            EReference eReference = eCoreFactory.createEReference();
            eReference.setName(feature.getName());
            eReference.setUpperBound(upperBound);

            eClass.getEStructuralFeatures().add(eReference);
            eReference.setEType(eType);
            eReference.setEGenericType(eGenericType);

            if (m.getAnnotation(Containment.class) != null) {
              eReference.setContainment(true);
            }
          }
        }
      } else {
        EOperation eOperation = eCoreFactory.createEOperation();

        eOperation.setName(m.getName());

        eOperation.setUpperBound(upperBound);

        if (!void.class.equals(returnType) && !Void.class.equals(returnType)) {
          eOperation.setEType(eType);
          eOperation.setEGenericType(eGenericType);
        }

        eClass.getEOperations().add(eOperation);
        Class<?>[] types = m.getParameterTypes();

        for (int i = 0; i < types.length; i++) {
          if (List.class.equals(types[i])) {
            System.out.println(m);
          }

          EClassifier eParamType = getEClassifier(types[i]);

          EParameter eParam = EcoreFactory.eINSTANCE.createEParameter();
          eParam.setEType(eParamType);

          eOperation.getEParameters().add(eParam);

        }
      }

    }

    return eClass;
  }

  public EClassifier getEClassifier(final Class<?> cls) {
    if (cls.isPrimitive() || "java.lang".equals(cls.getPackage().getName())) {
      for (EClassifier c : EcorePackage.eINSTANCE.getEClassifiers()) {
        if (cls.equals(c.getInstanceClass())) {
          return c;
        }
      }
      return null;
    }

    if (cls.equals(String.class)) {
      return EcorePackage.eINSTANCE.getEString();
    }

    if (cls.isEnum()) {
      return getEEnum(cls);
    } else {
      return getEClass(cls);
    }
  }

  public EEnum getEEnum(final Class<?> cls) {
    EPackage ePackage = getEPackage(cls);
    String classifierName = ClassUtil.getClassifierName(cls);
    EEnum eEnum = (EEnum) ePackage.getEClassifier(classifierName);
    if (eEnum != null) {
      return eEnum;
    }
    Class<? extends Enum> eNumClass = (Class<? extends Enum>) cls;
    eEnum = eCoreFactory.createEEnum();
    eEnum.setName(classifierName);
    eEnum.setInstanceClass(cls);
    ePackage.getEClassifiers().add(eEnum);
    for (Object o : EnumSet.allOf(eNumClass)) {
      Enum e = (Enum) o;
      EEnumLiteral eLiteral = eCoreFactory.createEEnumLiteral();
      eLiteral.setName(e.name());
      eLiteral.setValue(e.ordinal());
      eLiteral.setLiteral(e.name());
      eEnum.getELiterals().add(eLiteral);
    }
    return eEnum;
  }

  public EPackage getEPackage(final Class<?> cls) {
    if (cls.isPrimitive() || cls.getPackage().getName().equals("java.lang")) {
      return EcorePackage.eINSTANCE;
    }
    return getEPackage(cls.getPackage().getName());
  }

  public EPackage getEPackage(final Package pkg) {
    return getEPackage(pkg.getName());
  }

  public EPackage getEPackage(final String fullName) {
    if (fullName.equals("ecore")) {
      return EcorePackage.eINSTANCE;
    }
    String nsURI = getNsURI(fullName);
    EPackage ePackage = registry.getEPackage(nsURI);
    if (ePackage == null) {

      String prefix = getNsPrefix(fullName);
      try {
        String className =
            fullName + "." + prefix.substring(0, 1).toUpperCase() + prefix.substring(1) + "Package";
        Class<?> javaClass = Class.forName(className);
        Field field = javaClass.getField("eINSTANCE");
        Object result = field.get(null);
        ePackage = (EPackage) result;

        registry.put(nsURI, ePackage);

      } catch (Exception e) {
        Package pkg = Package.getPackage(fullName);
        if (pkg != null) {

          String name = fullName.substring(fullName.lastIndexOf(".") + 1);
          ePackage = eCoreFactory.createEPackage();
          ePackage.setNsURI(nsURI);
          ePackage.setNsPrefix(fullName);
          ePackage.setName(name);

          registry.put(nsURI, ePackage);

          String className =
              fullName + "." + prefix.substring(0, 1).toUpperCase() + prefix.substring(1)
                  + "AutoBeanFactory";
          try {
            Class<?> javaClass = Class.forName(className);

            for (Method m : javaClass.getDeclaredMethods()) {
              Type t = m.getGenericReturnType();
              Class cls = ClassUtil.getTypeArgument(t);
              if (cls.isEnum()) {
                getEEnum(cls);
              } else {
                getEClass(cls);
              }
            }
          } catch (ClassNotFoundException notfound) {
            // TODO ignore exception
            notfound.printStackTrace();
          }

          try {
            Resource resource = getModel(name);
            resource.getContents().add(ePackage);
            resource.save(null);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
      }
    }

    return ePackage;
  }

  public Resource getModel(final String fileName) {
    Resource resource;
    File file = new File("model/" + fileName + ".ecore");
    URI uri = URI.createFileURI(file.getAbsolutePath());
    if (file.exists()) {
      file.delete();
    } else {
      File folder = file.getParentFile();
      if (!folder.exists()) {
        folder.mkdir();
      }
    }
    resource = resourceSet.createResource(uri);
    return resource;
  }

  @Override
  public String getUri(final Object object) {
    return ((Content) object).getUri();
  }

  public void parseAnnotations(final EModelElement eModel, final AnnotatedElement element) {
    for (Annotation anno : element.getAnnotations()) {
      String source = anno.annotationType().getName();
      EAnnotation eAnn = EcoreFactory.eINSTANCE.createEAnnotation();
      eAnn.setSource(source);

      eModel.getEAnnotations().add(eAnn);
      EMap details = eAnn.getDetails();

      for (Method f : anno.annotationType().getDeclaredMethods()) {

        Object value;
        try {
          value = f.invoke(anno);
          if (value != null) {
            if (value instanceof Class) {
              value = ((Class) value).getName();
            }
            details.put(f.getName(), value.toString());
          }
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  public void save() {
    try {
      getData().save(null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void save(final Object obj) {
    getData().getContents().add((EObject) obj);
    save();
  }
}