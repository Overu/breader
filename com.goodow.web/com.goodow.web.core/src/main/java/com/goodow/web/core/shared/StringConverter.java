package com.goodow.web.core.shared;

public abstract class StringConverter<T> {

  public abstract T convertFrom(String source);

  public String convertTo(final T target) {
    return target == null ? null : target.toString();
  }

}
