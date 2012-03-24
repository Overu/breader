package org.cloudlet.web.service.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class ClassUtil {

  public static Class getClass(String className) {
    if (className != null) {
      try {
        return Class.forName(className);
      } catch (ClassNotFoundException e) {
        // e.printStackTrace();
      }
    }
    return null;
  }

  public static String getClassifierName(Class<?> cls) {
    String className = cls.getName();
    int index = className.lastIndexOf(".");
    return className.substring(index + 1);
  }

  public static Class<?> getInstanceClass(EClass eClass) {
    Class<?> result = eClass.getInstanceClass();
    if (result == null) {
      if (eClass.getESuperTypes().isEmpty()) {
        return EObject.class;
      }
      return getInstanceClass(eClass.getESuperTypes().get(0));
    }
    return result;
  }

  public static Class<?> getInstanceClass(EClassifier eClassifier) {
    if (eClassifier instanceof EClass) {
      return getInstanceClass(((EClass) eClassifier).getESuperTypes().get(0));
    }
    return eClassifier.getInstanceClass();
  }

  public static Class<?> getTypeArgument(Class<?> cls) {
    return getTypeArgument(cls, null);
  }

  public static <T> Class<? extends T> getTypeArgument(Class<?> cls, Class<T> subClass) {
    for (TypeVariable tv : cls.getTypeParameters()) {
      for (Type upperBoundType : tv.getBounds()) {
        if (upperBoundType instanceof Class) {
          Class<?> wildcardClass = (Class<?>) upperBoundType;
          if (subClass == null || wildcardClass.isAssignableFrom(subClass)) {
            return (Class<? extends T>) wildcardClass;
          }
        }
      }
    }
    Type genericType = cls.getGenericSuperclass();
    if (genericType != null) {
      return getTypeArgument(genericType, subClass);
    }

    for (Type genericInterface : cls.getGenericInterfaces()) {
      Class<? extends T> result = getTypeArgument(genericInterface, subClass);
      if (result != null) {
        return result;
      }
    }

    return null;
  }

  public static Class<?> getTypeArgument(Type genericType) {
    return getTypeArgument(genericType, null);
  }

  public static <T> Class<? extends T> getTypeArgument(Type genericType, Class<T> subClass) {
    if (genericType instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) genericType;
      for (int i = 0; i < pt.getActualTypeArguments().length; i++) {
        Type t = pt.getActualTypeArguments()[i];
        if (t instanceof Class) {
          Class<?> typeArg = (Class<?>) t;
          if (subClass == null || typeArg.isAssignableFrom(subClass)) {
            return (Class<? extends T>) typeArg;
          }
        } else if (t instanceof WildcardType) {
          Type wt = ((WildcardType) t).getUpperBounds()[0];
          if (wt instanceof Class) {
            Class<?> wildcardClass = (Class<?>) wt;
            if (subClass == null || wildcardClass.isAssignableFrom(subClass)) {
              return (Class<? extends T>) wildcardClass;
            }
          }
        } else if (t instanceof ParameterizedType) {
          ParameterizedType ppt = (ParameterizedType) t;
          Class<?> rt = (Class<?>) ppt.getRawType();
          // TODO check ppt.getActualTypeArguments();
          if (subClass == null || rt.isAssignableFrom(subClass)) {
            return (Class<? extends T>) rt;
          }
        }
      }
      Class<?> rt = (Class<?>) pt.getRawType();
      return getTypeArgument(rt.getGenericSuperclass(), subClass);
    }
    return null;
  }
}
