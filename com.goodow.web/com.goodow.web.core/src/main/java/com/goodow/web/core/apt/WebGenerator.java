package com.goodow.web.core.apt;

import com.goodow.web.core.client.ClientWebService;
import com.goodow.web.core.jpa.JpaWebService;
import com.goodow.web.core.shared.Accessor;
import com.goodow.web.core.shared.EntityInfo;
import com.goodow.web.core.shared.EnumInfo;
import com.goodow.web.core.shared.Factory;
import com.goodow.web.core.shared.OperationInfo;
import com.goodow.web.core.shared.Package;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.ValueInfo;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import javax.xml.bind.annotation.XmlType;

@SupportedAnnotationTypes(value = {"*"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class WebGenerator extends AbstractProcessor {

  public static final String TYPES = "Types";

  public static final String OPERATIONS = "Operations";

  public static String getModuleFullName(final PackageElement pkg) {
    String pkgName = pkg.getQualifiedName().toString();
    String prefix = getPrefix(pkg);
    String pkgClassName = prefix + "Package";
    return pkgName + "." + pkgClassName;
  }

  private static String getPrefix(final PackageElement pkg) {
    String pkgName = pkg.getQualifiedName().toString();
    return getPrefix(pkgName);
  }

  private static String getPrefix(String pkgName) {
    pkgName = pkgName.substring(0, pkgName.lastIndexOf("."));
    String simplePkgName = pkgName.substring(pkgName.lastIndexOf(".") + 1);
    String prefix = simplePkgName.substring(0, 1).toUpperCase() + simplePkgName.substring(1);
    return prefix;
  }

  private Filer filer;

  private Messager messager;

  private Elements elements;

  private Map<String, TypeElement> typeElements;

  @Override
  public void init(final ProcessingEnvironment env) {
    filer = env.getFiler();
    messager = env.getMessager();
    elements = env.getElementUtils();
    typeElements = new HashMap<String, TypeElement>();
  }

  @Override
  public boolean process(final Set<? extends TypeElement> annotations,
      final RoundEnvironment roundEnv) {

    for (TypeElement type : ElementFilter.typesIn(roundEnv.getRootElements())) {
      typeElements.put(type.getQualifiedName().toString(), type);
    }

    for (PackageElement pkg : ElementFilter.packagesIn(roundEnv.getRootElements())) {
      generateFactory(pkg);
      generatePackage(pkg);
      generateModule(pkg, ClientWebService.class);
      generateModule(pkg, JpaWebService.class);
    }

    return false;
  }

  SourceWriter openWriter(final String pkgName, final String simpleName,
      final Element... originatingElements) throws IOException {
    String className = pkgName + "." + simpleName;
    JavaFileObject genFile = filer.createSourceFile(className, originatingElements);
    SourceWriter result = new SourceWriter(className, genFile.openWriter());
    return result;
  }

  private void generateClientService(final TypeElement entityType, final String module,
      final boolean async, final Class<?> baseServiceClass) {
    PackageElement pkg = (PackageElement) entityType.getEnclosingElement();
    String pkgName = pkg.getQualifiedName().toString();
    String prefix = getPrefix(pkg);
    String pkgSimpleName = prefix + "Package";
    int index = pkgName.lastIndexOf(".");
    String implPackageName = pkgName.substring(0, index) + "." + module.toLowerCase();

    TypeElement serviceType = getServiceType(entityType);
    if (serviceType == null) {
      return;
    }

    try {
      String serviceName = module + serviceType.getSimpleName().toString();

      String implClassName = implPackageName + "." + serviceName;
      if ("ServerLibraryService".equals(serviceName)) {
        System.out.println("debug");
      }

      TypeElement typeEl = typeElements.get(implClassName);
      if (typeEl != null) {
        for (AnnotationMirror m : typeEl.getAnnotationMirrors()) {
          System.out.println(m);
        }
        for (Element e : typeEl.getEnclosedElements()) {
          System.out.println(e);
        }
        return;
      }

      SourceWriter w = openWriter(implPackageName, serviceName, entityType);

      w.importPackage(pkgName);
      w.importPackage(baseServiceClass.getPackage());
      w.importPackage(Request.class.getPackage());

      String typeName = entityType.getQualifiedName().toString();

      w.print("public class ").print(serviceName);

      List<? extends TypeParameterElement> typeVars = serviceType.getTypeParameters();
      if (!typeVars.isEmpty()) {
        w.print("<");
        boolean first = true;
        for (TypeParameterElement tpe : typeVars) {
          if (first) {
            first = false;
          } else {
            w.print(", ");
          }
          w.print(tpe.getSimpleName().toString()).print(" extends ");
          for (TypeMirror b : tpe.getBounds()) {
            w.type(b.toString());
            break;
          }
        }
        w.print(">");
      }

      w.print(" extends ").type(baseServiceClass);
      w.print("<");
      if (!typeVars.isEmpty()) {

        boolean first = true;
        for (TypeParameterElement tpe : typeVars) {
          if (first) {
            first = false;
          } else {
            w.print(", ");
          }
          w.print(tpe.getSimpleName().toString());
        }
      } else {
        w.type(typeName);
      }
      w.print(">");

      if (!async) {
        w.print(" implements ").type(serviceType.getQualifiedName().toString());

        if (!typeVars.isEmpty()) {
          w.print("<");
          boolean first = true;
          for (TypeParameterElement tpe : typeVars) {
            if (first) {
              first = false;
            } else {
              w.print(", ");
            }
            w.print(tpe.getSimpleName().toString());
          }
          w.print(">");
        }

      }

      w.print(" {");

      w.indent();

      for (ExecutableElement method : ElementFilter.methodsIn(serviceType.getEnclosedElements())) {

        TypeMirror rt = method.getReturnType();
        w.println();

        if (!async) {
          w.println("@Override");
        }

        w.print("public ");

        if (async) {
          w.type(Request.class).print("<");
          if (rt.getKind() == TypeKind.VOID) {
            w.print("Void");
          } else {
            w.type(rt.toString());
          }
        } else {
          w.type(rt.toString());
        }

        if (async) {
          w.print(">");
        }
        w.print(" ").print(method.getSimpleName()).print("(");

        boolean first = true;
        for (VariableElement p : method.getParameters()) {
          if (first) {
            first = false;
          } else {
            w.print(", ");
          }
          w.type(p.asType().toString()).print(" ").print(p.getSimpleName());
        }
        w.println(") {");

        w.indent();

        if (async) {
          w.print("return ");
        } else if (rt.getKind() != TypeKind.VOID) {
          w.print("return ");
        }

        w.print("invoke(").print(pkgSimpleName).print(".").print(getOperationName(method));
        for (VariableElement p : method.getParameters()) {
          w.print(", ");
          w.print(p.getSimpleName());
        }
        w.println(");");
        w.outdent();

        w.println("}");
      }

      w.outdent();

      w.println("}");
      w.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String generateFactory(final PackageElement pkg) {
    String pkgName = pkg.getQualifiedName().toString();
    String prefix = getPrefix(pkg);
    String factoryName = prefix + "Factory";

    try {
      SourceWriter w = openWriter(pkgName, factoryName, pkg);

      w.importPackage(WebObject.class.getPackage());
      w.importPackage(Singleton.class.getPackage());
      w.println();

      w.print("@").print(Singleton.class.getSimpleName()).println("");
      w.print("public class ").print(factoryName).print(" extends ").type(Factory.class).println(
          " {");

      w.indent();

      for (TypeElement xmlType : getXmlTypes(pkg)) {
        String typeName = xmlType.getSimpleName().toString();

        if (!xmlType.getModifiers().contains(Modifier.ABSTRACT)) {
          w.println();
          w.print("@").println(Inject.class.getSimpleName());
          w.print("public static ").print(Provider.class.getSimpleName()).print("<")
              .print(typeName).print("> ").print(typeName).println(";");
        }

        // TypeElement serviceType = getServiceType(resType);
        // if (serviceType != null) {
        // w.println();
        // w.print("@").println(Inject.class.getSimpleName());
        // String serviceTypeName =
        // serviceType.getQualifiedName().toString();
        // String serviceInstanceName = typeName +
        // Service.class.getSimpleName();
        // w.print("public static ").type(serviceTypeName).print(" ").print(serviceInstanceName)
        // .println(";");
        // }
      }

      w.outdent();

      w.print("}");
      w.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return factoryName;
  }

  private String generateModule(final PackageElement pkg, final Class<?> baseSerivceClass) {

    String basePackageName = baseSerivceClass.getPackage().getName();
    String module = basePackageName.substring(basePackageName.lastIndexOf(".") + 1);
    module = module.substring(0, 1).toUpperCase() + module.substring(1);
    boolean async = ClientWebService.class.equals(baseSerivceClass);

    if (!async) {
      return null;
    }

    String sharedPkgName = pkg.getQualifiedName().toString();
    int index = sharedPkgName.lastIndexOf(".");
    String pkgName = sharedPkgName.substring(0, index) + "." + module.toLowerCase();
    String prefix = getPrefix(pkg);
    String moduleName = prefix + module + "Module";
    String pkgSimpleName = prefix + "Package";
    String factorySimpleName = prefix + "Factory";

    String coreSharedPkg = WebObject.class.getPackage().getName();
    String corePkg = coreSharedPkg.substring(0, coreSharedPkg.lastIndexOf("."));
    String coreModulePkg = corePkg + "." + module.toLowerCase();
    String baseModuleName = coreModulePkg + "." + module + "Module";

    try {
      SourceWriter w = openWriter(pkgName, moduleName, pkg);

      w.importPackage(WebObject.class.getPackage());
      w.importPackage(Singleton.class.getPackage());
      w.importPackage(coreModulePkg);
      w.importPackage(sharedPkgName);
      String corePackage = WebObject.class.getPackage().getName();
      w.importPackage(corePackage.substring(0, corePackage.lastIndexOf(".")) + "."
          + module.toLowerCase());
      w.println();

      w.print("@").print(Singleton.class.getSimpleName()).println("");
      w.print("public class ").print(moduleName).print(" extends ").type(baseModuleName).println(
          " {");

      w.indent();

      w.println("public static class Startup {");

      w.indent();
      w.annotation(Inject.class);
      w.print("public Startup(final ").print(pkgSimpleName).println(
          " pkg, final WebPlatform platform) {");
      w.indent();
      w.println("platform.getPackages().put(pkg.getName(), pkg);");
      w.outdent();
      w.println("}");
      w.outdent();

      w.println("}");

      w.println();
      w.annotation(Override.class);
      w.println("protected void configure() {");

      w.indent();
      w.print("requestStaticInjection(").print(factorySimpleName).println(".class);");
      w.println("bind(Startup.class).asEagerSingleton();");

      if (!async) {
        for (TypeElement entityType : getXmlTypes(pkg)) {

          TypeElement serviceType = getServiceType(entityType);

          if (serviceType == null) {
            continue;
          }

          String typeName = entityType.getSimpleName().toString();

          String serviceInstanceName = typeName + "WebService";

          // if (serviceType == null) {
          // w.type(Service.class.getName()).print("<").type(entityType.getQualifiedName().toString())
          // .print(">");
          // } else {
          w.print("bind(").type(serviceType.getQualifiedName().toString()).print(".class).to(");
          // }
          w.type(pkgName + "." + module + serviceType.getSimpleName().toString());
          w.print(".class);").println();
        }
      }

      w.outdent();

      w.println("}");

      w.outdent();

      w.print("}");
      w.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (TypeElement webTypeEl : getXmlTypes(pkg)) {
      generateClientService(webTypeEl, module, async, baseSerivceClass);
    }

    return moduleName;
  }

  private String generatePackage(final PackageElement pkg) {
    String pkgName = pkg.getQualifiedName().toString();
    String prefix = getPrefix(pkg);
    String pkgSimpleName = prefix + "Package";
    String factorySimpleName = prefix + "Factory";

    try {
      SourceWriter w = openWriter(pkgName, pkgSimpleName, pkg);

      w.importPackage(WebObject.class.getPackage());
      w.println();

      w.print("@").print(Singleton.class.getName()).println();
      String basePackageName = pkgName + ".Base" + pkgSimpleName;
      TypeElement basePackageEl = typeElements.get(basePackageName);
      if (basePackageEl == null) {
        basePackageName = Package.class.getName();
      }
      w.print("public class ").print(pkgSimpleName).print(" extends ").type(basePackageName).print(
          " {").println();

      w.indent();

      for (TypeElement xmlType : getXmlTypes(pkg)) {
        String typeName = xmlType.getQualifiedName().toString();

        boolean isFinal = xmlType.getModifiers().contains(Modifier.FINAL);

        String superAccessor = null;

        if (!WebObject.class.getName().equals(typeName)) {
          TypeMirror superClass = xmlType.getSuperclass();
          String superClassFullName = superClass.toString();
          int index = superClassFullName.lastIndexOf(".");
          String superPkg = superClassFullName.substring(0, index);
          String superClassSimplename = superClassFullName.substring(index + 1);

          String superPkgPrefix = getPrefix(superPkg);
          superAccessor =
              superPkg + "." + superPkgPrefix + Package.class.getSimpleName() + "."
                  + superClassSimplename + Accessor.class.getSimpleName();
        }

        w.print("public static class ").print(xmlType.getSimpleName().toString()).print(
            Accessor.class.getSimpleName());
        if (!isFinal) {
          w.print("<T extends ").type(typeName).print(">");
        }

        if (superAccessor != null) {
          w.print(" extends ").print(superAccessor);
          if (isFinal) {
            w.print("<").type(typeName).print(">");
          } else {
            w.print("<T>");
          }
        } else {
          w.print(" implements ").type(Accessor.class).print("<T>");
        }
        w.print(" {").println();

        w.indent();

        // Getter method
        w.annotation(Override.class);
        w.print("public Object getProperty(");
        if (isFinal) {
          w.type(typeName);
        } else {
          w.print("T");
        }
        w.println(" entity, Property property) {");

        w.indent();

        boolean first = true;
        for (VariableElement field : ElementFilter.fieldsIn(xmlType.getEnclosedElements())) {
          if (field.getModifiers().contains(Modifier.TRANSIENT)
              || field.getModifiers().contains(Modifier.STATIC)) {
            continue;
          }
          String fieldName = field.getSimpleName().toString();
          if (fieldName.startsWith("_")) {
            fieldName = fieldName.substring(1);
          }

          TypeMirror typeMirror = field.asType();

          boolean isBoolean =
              Boolean.class.getName().equals(typeMirror.toString())
                  || boolean.class.getName().equals(typeMirror.toString());

          if (first) {
            first = false;
          } else {
            w.print(" else ");
          }
          w.print("if (\"").print(fieldName).println("\".equals(property.getName())) {");
          w.indent();
          w.print("return entity.").print(isBoolean ? "is" : "get").print(
              fieldName.substring(0, 1).toUpperCase()).print(fieldName.substring(1)).println("();");
          w.outdent();
          w.print("}");
        }

        if (!first) {
          w.println(" else {");
          w.indent();
        }

        if (superAccessor == null) {
          w.println("return null;");
        } else {
          w.println("return super.getProperty(entity, property);");
        }

        if (!first) {
          w.outdent();
          w.println("}");
        }

        w.outdent();

        w.println("}");

        // Setter method
        w.annotation(Override.class);
        w.print("public void setProperty(");
        if (isFinal) {
          w.type(typeName);
        } else {
          w.print("T");
        }
        w.println(" entity, Property property, Object value) {");

        w.indent();

        first = true;
        for (VariableElement field : ElementFilter.fieldsIn(xmlType.getEnclosedElements())) {
          if (field.getModifiers().contains(Modifier.TRANSIENT)
              || field.getModifiers().contains(Modifier.STATIC)) {
            continue;
          }
          String fieldName = field.getSimpleName().toString();
          if (fieldName.startsWith("_")) {
            fieldName = fieldName.substring(1);
          }

          TypeMirror typeMirror = field.asType();

          String paramTypeFullName = typeMirror.toString();

          switch (typeMirror.getKind()) {
            case BOOLEAN:
              paramTypeFullName = Boolean.class.getName();
              break;
            case BYTE:
              paramTypeFullName = Byte.class.getName();
              break;
            case SHORT:
              paramTypeFullName = Short.class.getName();
              break;
            case INT:
              paramTypeFullName = Integer.class.getName();
              break;
            case LONG:
              paramTypeFullName = Long.class.getName();
              break;
            case CHAR:
              paramTypeFullName = Character.class.getName();
              break;
            case FLOAT:
              paramTypeFullName = Float.class.getName();
              break;
            case DOUBLE:
              paramTypeFullName = Double.class.getName();
              break;
          }

          if (first) {
            first = false;
          } else {
            w.print(" else ");
          }
          w.print("if (\"").print(fieldName).println("\".equals(property.getName())) {");
          w.indent();
          w.print("entity.set").print(fieldName.substring(0, 1).toUpperCase()).print(
              fieldName.substring(1)).print("((").type(paramTypeFullName).println(") value);");
          w.outdent();
          w.print("}");
        }

        if (!first) {
          w.println(" else {");
          w.indent();
        }

        if (superAccessor == null) {
          w.println();
        } else {
          w.println("super.setProperty(entity, property, value);");
        }

        if (!first) {
          w.outdent();
          w.println("}");
        }

        w.outdent();

        w.println("}");

        w.outdent();

        w.println("}");
      }

      for (TypeElement xmlType : getXmlTypes(pkg)) {
        String typeName = xmlType.getSimpleName().toString();
        String serviceClass = getServiceClass(xmlType);

        boolean isAbstract = xmlType.getModifiers().contains(Modifier.ABSTRACT);

        w.print("public static final ").type(EntityInfo.class).print(" ").print(typeName).print(
            " = new ").type(EntityInfo.class).print("(");
        // w.print("\"").print(typeName).print("\"");
        // w.print(", ");

        w.type(xmlType.getQualifiedName().toString()).print(".class");

        w.print(", ");
        if (isAbstract) {
          w.print("null");
        } else {
          w.print(factorySimpleName).print(".").print(typeName);
        }

        w.print(", ");
        if (serviceClass != null) {
          w.type(serviceClass).print(".class");
        } else {
          w.print("null");
        }

        // w.print(", ");
        // TypeElement serviceType = getServiceType(resType);
        // if (serviceType != null) {
        // String serviceInstance = typeName +
        // Service.class.getSimpleName();
        // w.print(factorySimpleName).print(".").print(serviceInstance);
        // } else {
        // w.print("null");
        // }

        w.println(");");
      }

      List<String> valueTypes = new ArrayList<String>();
      if (basePackageEl != null) {
        for (VariableElement vt : ElementFilter.fieldsIn(basePackageEl.getEnclosedElements())) {
          TypeMirror tm = vt.asType();

          if (tm instanceof DeclaredType) {
            DeclaredType dt = (DeclaredType) tm;
            Element e = dt.asElement();

            if (ValueInfo.class.getName().equals(e.toString())) {
              valueTypes.add(vt.getSimpleName().toString());
            }
          }
        }
      }

      for (TypeElement enumType : getEnumTypes(pkg)) {

        String name = enumType.getSimpleName().toString();
        valueTypes.add(name);

        w.print("public static final ").type(EnumInfo.class).print("<").type(name).print("> ")
            .print(name).print(" = new ").type(EnumInfo.class).print("<").type(name).print(">(");
        // w.print("\"").print(name).print("\"");
        // w.print(", ");

        w.type(name.toString()).print(".class");

        // w.print(", new ").type(EnumConverter.class.getName()).print("<").print(name).print(">(")
        // .print(name).print(".class)");

        w.println(");");

      }

      for (TypeElement xmlType : getXmlTypes(pkg)) {
        String typeName = xmlType.getSimpleName().toString();
        TypeElement serviceType = getServiceType(xmlType);
        if (serviceType != null
            && !serviceType.getQualifiedName().toString().equals(WebService.class.getName())) {
          for (ExecutableElement method : ElementFilter
              .methodsIn(serviceType.getEnclosedElements())) {
            TypeMirror rt = method.getReturnType();
            String returnTypeName = getReturnTypeName(rt);
            String operationName = getOperationName(method);
            w.print("public static final ").type(OperationInfo.class).print("<");
            w.type(returnTypeName);
            w.print("> ").print(operationName).print(" = new ").type(OperationInfo.class)
                .print("<").type(returnTypeName).print(">(");
            w.print("\"").print(method.getSimpleName()).print("\"");
            w.println(");");
          }
        }
      }

      w.print("public ").print(pkgSimpleName).print("() {").println();

      w.indent();

      w.print("setName(\"").print(pkgName).println("\");");
      w.print("addEntityTypes(");
      boolean first = true;
      for (TypeElement xmlType : getXmlTypes(pkg)) {
        if (first) {
          first = false;
        } else {
          w.print(", ");
        }
        w.print(xmlType.getSimpleName()).print(".as()");
      }
      w.println(");");

      w.print("addValueTypes(");
      first = true;
      for (String valueType : valueTypes) {

        if (first) {
          first = false;
        } else {
          w.print(", ");
        }
        w.print(valueType).print(".as()");
      }
      w.println(");");

      for (TypeElement xmlType : getXmlTypes(pkg)) {

        String typeName = xmlType.getSimpleName().toString();

        TypeMirror superClass = xmlType.getSuperclass();
        String superClassFullName = superClass.toString();
        if (!Object.class.getName().equals(superClassFullName)) {
          int index = superClassFullName.lastIndexOf(".");
          String superPkg = superClassFullName.substring(0, index);
          String superClassSimplename = superClassFullName.substring(index + 1);

          String superPkgPrefix = getPrefix(superPkg);

          w.print(typeName).print(".as().setSuperType(").print(superPkg).print(".").print(
              superPkgPrefix).print(Package.class.getSimpleName()).print(".").print(
              superClassSimplename).println(".as());");
        }

        w.print(typeName).print(".as().setAccessor(new ").print(typeName).print(
            Accessor.class.getSimpleName()).println("());");

        for (VariableElement field : ElementFilter.fieldsIn(xmlType.getEnclosedElements())) {
          if (field.getModifiers().contains(Modifier.TRANSIENT)
              || field.getModifiers().contains(Modifier.STATIC)) {
            continue;
          }
          w.print("addProperty(").print(typeName).print(".as()");
          w.print(", \"").print(field.getSimpleName()).print("\"");

          TypeMirror typeMirror = field.asType();
          boolean many = false;

          if (typeMirror instanceof DeclaredType) {
            DeclaredType dt = (DeclaredType) typeMirror;
            TypeElement e = (TypeElement) dt.asElement();
            if (List.class.getName().equals(e.getQualifiedName().toString())) {
              typeMirror = dt.getTypeArguments().get(0);
              many = true;
            }
          }

          TypeKind typeKind = typeMirror.getKind();

          String paramTypeFullName;
          String paramPkgName;
          String paramTypeSimpleName;

          paramTypeFullName = typeMirror.toString();
          if (typeKind.isPrimitive()) {
            paramPkgName = WebObject.class.getPackage().getName();
            paramTypeSimpleName = paramTypeFullName.toUpperCase();
          } else {
            paramPkgName = paramTypeFullName.substring(0, paramTypeFullName.lastIndexOf("."));
            paramTypeSimpleName = paramTypeFullName.substring(paramPkgName.length() + 1);
            if (paramPkgName.equals("java.lang")) {
              paramPkgName = WebObject.class.getPackage().getName();
            }
          }

          String paramPkgPrefix = getPrefix(paramPkgName);
          String paramPkgSimpleName = paramPkgPrefix + "Package";
          w.print(", ").type(paramPkgName + "." + paramPkgSimpleName).print(".").print(
              paramTypeSimpleName).print(".as()");

          w.print(", ").print(Boolean.toString(many));

          w.println(");");
        }
        w.println();

        TypeElement serviceType = getServiceType(xmlType);
        if (serviceType != null
            && !serviceType.getQualifiedName().toString().equals(WebService.class.getName())) {
          Map<String, List<? extends TypeMirror>> typeVars =
              new HashMap<String, List<? extends TypeMirror>>();
          for (TypeParameterElement tpe : serviceType.getTypeParameters()) {
            String s = tpe.getSimpleName().toString();
            Element e = tpe.getGenericElement();
            typeVars.put(s, tpe.getBounds());
          }

          w.print("addOperations(").print(typeName).print(".as()");
          for (ExecutableElement method : ElementFilter
              .methodsIn(serviceType.getEnclosedElements())) {
            w.print(", ");
            w.print(getOperationName(method));
          }
          w.println(");");

          for (ExecutableElement method : ElementFilter
              .methodsIn(serviceType.getEnclosedElements())) {

            if (method.getParameters().size() > 0) {

              for (VariableElement pEl : method.getParameters()) {
                w.print("addParameter(").print(getOperationName(method)).print(".as()");
                w.print(", \"").print(pEl.getSimpleName()).print("\"");
                TypeMirror typeMirror = pEl.asType();
                TypeKind typeKind = typeMirror.getKind();

                String paramTypeFullName;
                String paramPkgName;
                String paramTypeSimpleName;

                paramTypeFullName = typeMirror.toString();
                if (typeKind == TypeKind.TYPEVAR) {
                  List<? extends TypeMirror> bounds = typeVars.get(paramTypeFullName);
                  w.print(", null");
                } else {
                  if (typeKind.isPrimitive()) {
                    paramPkgName = WebObject.class.getPackage().getName();
                    paramTypeSimpleName = paramTypeFullName.toUpperCase();
                  } else {
                    if (paramTypeFullName.lastIndexOf(".") < 0) {
                      System.out.println("debug");
                      // for (TypeParameterElement tpe : method.getTypeParameters()) {
                      // String s = tpe.getSimpleName().toString();
                      // Element e = tpe.getGenericElement();
                      // System.out.println(s);
                      // }
                    }
                    paramPkgName =
                        paramTypeFullName.substring(0, paramTypeFullName.lastIndexOf("."));
                    paramTypeSimpleName = paramTypeFullName.substring(paramPkgName.length() + 1);
                    if (paramPkgName.equals("java.lang")) {
                      paramPkgName = WebObject.class.getPackage().getName();
                    }
                  }

                  String paramPkgPrefix = getPrefix(paramPkgName);
                  String paramPkgSimpleName = paramPkgPrefix + "Package";
                  w.print(", ").type(paramPkgName + "." + paramPkgSimpleName).print(".").print(
                      paramTypeSimpleName).print(".as()");
                }

                w.println(");");

              }
              w.println();
            }
          }
        }
      }

      w.outdent();

      w.print("}").println();
      w.outdent();

      w.print("}").println();
      w.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return pkgSimpleName;
  }

  private Set<TypeElement> getEnumTypes(final PackageElement pkg) {
    Set<TypeElement> result = new HashSet<TypeElement>();
    for (TypeElement type : ElementFilter.typesIn(pkg.getEnclosedElements())) {
      if (type.getKind() == ElementKind.ENUM) {
        result.add(type);
      }
    }
    return result;
  }

  private String getOperationName(final ExecutableElement method) {
    String operationName =
        method.getEnclosingElement().getSimpleName() + "_" + method.getSimpleName().toString();
    return operationName;
  }

  private String getPropertyName(final ExecutableElement ee) {
    String methodName = ee.getSimpleName().toString();
    String propName = null;
    if (methodName.startsWith("set") && ee.getParameters().size() == 1) {
      propName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
    } else if (methodName.startsWith("get") && ee.getParameters().size() == 0) {
      propName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
    } else if (methodName.startsWith("is") && ee.getParameters().size() == 0) {
      propName = methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
    }
    return propName;
  }

  private String getReturnTypeName(final TypeMirror rt) {
    switch (rt.getKind()) {
      case VOID:
        return Void.class.getName();
      case BOOLEAN:
        return Boolean.class.getName();
      case INT:
        return Integer.class.getName();
      case LONG:
        return Long.class.getName();
      default:
        return rt.toString();
    }
  }

  private String getServiceClass(final TypeElement entityType) {
    TypeElement serviceType = getServiceType(entityType);
    if (serviceType == null) {
      return null;
    } else {
      return serviceType.getQualifiedName().toString();
    }
  }

  private TypeElement getServiceType(final TypeElement entityType) {
    TypeElement serviceType;
    if (entityType.getQualifiedName().toString().equals(WebEntity.class.getName())) {
      serviceType = typeElements.get(WebService.class.getName());
    } else {
      serviceType = typeElements.get(entityType.getQualifiedName() + "Service");
    }
    return serviceType;
  }

  private Set<TypeElement> getXmlTypes(final PackageElement pkg) {
    Set<TypeElement> servicesTypes = new HashSet<TypeElement>();
    for (TypeElement type : ElementFilter.typesIn(pkg.getEnclosedElements())) {
      if (type.getAnnotation(XmlType.class) != null) {
        servicesTypes.add(type);
      }
    }
    return servicesTypes;
  }
}