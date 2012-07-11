package com.goodow.web.core.shared;

public class EnumInfo<T extends Enum<T>> extends ValueInfo<T> {

  private final Class<T> enumClass;

  public EnumInfo(final Class<T> clazz) {
    super(clazz);
    this.enumClass = clazz;
  }

  @Override
  public T convertFrom(final String stringValue) {
    return Enum.valueOf(enumClass, stringValue);
  }

  @Override
  public String convertTo(final T instanceValue) {
    return instanceValue.name();
  }
}
