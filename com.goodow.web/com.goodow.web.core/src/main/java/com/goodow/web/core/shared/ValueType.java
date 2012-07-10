package com.goodow.web.core.shared;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class ValueType extends WebType implements Wrapper<ValueType> {

  private transient StringConverter<?> textReader;

  @Override
  public ValueType as() {
    return this;
  }

  public <T> StringConverter<T> getTextReader() {
    return (StringConverter<T>) textReader;
  }

  public void setTextReader(final StringConverter<?> textReader) {
    this.textReader = textReader;
  }

}
