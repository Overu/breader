package com.goodow.web.core.shared;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class ValueType extends Type implements Wrapper<ValueType> {

  @Override
  public ValueType as() {
    return this;
  }

  @Override
  public EntityType type() {
    return CorePackage.ValueType.as();
  }

}
