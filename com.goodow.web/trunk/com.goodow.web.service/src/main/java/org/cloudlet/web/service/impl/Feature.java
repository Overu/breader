package org.cloudlet.web.service.impl;

import java.lang.reflect.Method;

public class Feature {

  public static Feature parse(final Method method) {
    Feature feature = null;
    if (method.getParameterTypes().length == 0 && !void.class.equals(method.getReturnType())) {
      String name = null;
      String methodName = method.getName();
      if ((methodName.startsWith("get")) && methodName.length() > 3) {
        name = methodName.substring(3);
      } else if (methodName.startsWith("is") && methodName.length() > 2) {
        name = methodName.substring(2);
      }
      if (name != null) {
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        feature = new Feature();
        feature.setName(name);
        feature.setGetter(true);
      }
    } else if (method.getParameterTypes().length == 1 && void.class.equals(method.getReturnType())) {

      String methodName = method.getName();

      if ((methodName.startsWith("set")) && methodName.length() > 3) {
        String name = methodName.substring(3);
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        feature = new Feature();
        feature.setName(name);
        feature.setGetter(false);
      }
    }
    return feature;
  }

  private String name;

  private boolean getter;

  private Feature() {
  }

  public String getName() {
    return name;
  }

  public boolean isGetter() {
    return getter;
  }

  public void setGetter(final boolean getter) {
    this.getter = getter;
  }

  public void setName(final String name) {
    this.name = name;
  }

}
