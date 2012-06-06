package com.goodow.web.core.shared;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

@Singleton
public class WebPlatform {

  Map<String, Package> packages = new HashMap<String, Package>();

  public EntityType getEntityType(final String fullName) {
    Type type = getType(fullName);
    return (EntityType) type;
  }

  public Operation getOperation(final String fullName) {
    int index = fullName.lastIndexOf(".");
    String typeName = fullName.substring(0, index);
    EntityType pkg = getEntityType(typeName);
    if (pkg != null) {
      String simpleName = fullName.substring(index + 1);
      return pkg.getOperation(simpleName);
    }
    return null;

  }

  public Package getPackage(final String name) {
    return packages.get(name);
  }

  public Map<String, Package> getPackages() {
    return packages;
  }

  public Type getType(final String fullName) {
    int index = fullName.lastIndexOf(".");
    String pkgName = fullName.substring(0, index);
    Package pkg = getPackage(pkgName);
    if (pkg != null) {
      String simpleName = fullName.substring(index + 1);
      return pkg.getType(simpleName);
    }
    return null;
  }

}
