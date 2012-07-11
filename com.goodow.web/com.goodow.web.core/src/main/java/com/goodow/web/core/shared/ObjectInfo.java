package com.goodow.web.core.shared;

import com.goodow.web.core.shared.util.ClassUtil;

import com.google.inject.Provider;

public class ObjectInfo<T extends WebObject> implements Wrapper<ObjectType>, Provider<T> {

  private final ObjectType objectType;

  public ObjectInfo(final Class<T> clazz, final Class<? extends WebService> serviceClass) {
    objectType = new ObjectType();
    String name = ClassUtil.getSimpleName(clazz);
    objectType.setName(name);
    objectType.setJavaClass(clazz);
    objectType.setServiceClass(serviceClass);
    objectType.setProvider(this);
  }

  @Override
  public ObjectType as() {
    return objectType;
  }

  @Override
  public T get() {
    throw new UnsupportedOperationException();
  }

}