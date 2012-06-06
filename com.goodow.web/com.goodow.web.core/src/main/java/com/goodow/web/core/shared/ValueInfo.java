package com.goodow.web.core.shared;

import com.goodow.web.core.shared.util.ClassUtil;

public abstract class ValueInfo<T> implements Wrapper<ValueType>, TextReader<T>, TextWriter<T> {

  private final ValueType valueType;

  public ValueInfo(final Class<T> clazz) {
    valueType = CoreFactory.ValueType.get();
    String name = ClassUtil.getSimpleName(clazz);
    valueType.setName(name);
    valueType.setDefinitionClass(clazz);
    valueType.setTextReader(this);
    valueType.setTextWriter(this);
  }

  @Override
  public ValueType as() {
    return valueType;
  }

  // @Override
  @Override
  public String writeTo(final T target) {
    return target == null ? null : target.toString();
  }

}
