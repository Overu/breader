package com.goodow.web.core.shared;

import com.goodow.web.core.shared.util.ClassUtil;

import com.google.inject.Provider;

public class EntityInfo<T extends WebObject> implements Wrapper<ObjectType>, TextReader<T>,
    TextWriter<T> {

  private final ObjectType entityType;
  private final Provider<T> provider;

  public EntityInfo(final Class<T> clazz, final Provider<T> provider,
      final Class<? extends Service> serviceClass) {
    this.provider = provider;
    entityType = new ObjectType();
    String name = ClassUtil.getSimpleName(clazz);
    entityType.setName(name);
    entityType.setDefinitionClass(clazz);
    entityType.setTextReader(this);
    entityType.setTextWriter(this);
    entityType.setServiceClass(serviceClass);
    entityType.setProvider(provider);
  }

  @Override
  public ObjectType as() {
    return entityType;
  }

  public T create() {
    return provider.get();
  }

  @Override
  public T readFrom(final String stringValue) {
    if (stringValue == null) {
      return null;
    }
    T result = create();

    return result;
  }

  @Override
  public String writeTo(final T entity) {
    if (entity == null) {
      return null;
    }

    for (Property property : entityType.as().getProperties().values()) {

    }
    return entity.toString();
  }
}