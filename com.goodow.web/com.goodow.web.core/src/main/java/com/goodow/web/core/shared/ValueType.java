package com.goodow.web.core.shared;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class ValueType extends WebType implements Wrapper<ValueType> {

  private transient StringConverter<?> converter;

  @Override
  public ValueType as() {
    return this;
  }

  @SuppressWarnings("unchecked")
  public <T> StringConverter<T> getConverter() {
    return (StringConverter<T>) converter;
  }

  public void setConverter(final StringConverter<?> converter) {
    this.converter = converter;
  }

}
