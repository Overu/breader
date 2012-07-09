package com.goodow.web.core.shared;

import com.goodow.web.core.shared.util.ClassUtil;

import com.google.inject.Provider;

public class ObjectInfo<T extends WebObject> implements Wrapper<ObjectType>, TextReader<T>,
    TextWriter<T>, Provider<T> {

  private final ObjectType objectType;

  public ObjectInfo(final Class<T> clazz, final Class<? extends WebService> serviceClass) {
    objectType = new ObjectType();
    String name = ClassUtil.getSimpleName(clazz);
    objectType.setName(name);
    objectType.setJavaClass(clazz);
    objectType.setTextReader(this);
    objectType.setTextWriter(this);
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

  @Override
  public T readFrom(final String stringValue) {
    if (stringValue == null) {
      return null;
    }
    T result = get();

    return result;
  }

  @Override
  public String writeTo(final T entity) {
    if (entity == null) {
      return null;
    }

    for (Property property : objectType.as().getProperties().values()) {

    }
    return entity.toString();
  }
}