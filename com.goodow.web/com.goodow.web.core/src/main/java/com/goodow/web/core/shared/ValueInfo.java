package com.goodow.web.core.shared;

import com.goodow.web.core.shared.util.ClassUtil;

public abstract class ValueInfo<T> extends StringConverter<T> implements Wrapper<ValueType> {

  private final ValueType valueType;

  public ValueInfo(final Class<T> clazz) {
    valueType = new ValueType();
    String name = ClassUtil.getSimpleName(clazz);
    valueType.setName(name);
    valueType.setJavaClass(clazz);
    valueType.setConverter(this);
  }

  @Override
  public ValueType as() {
    return valueType;
  }

  // @Override
  @Override
  public String convertTo(final T target) {
    return target == null ? null : target.toString();
  }

}
