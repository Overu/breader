package com.goodow.web.core.shared;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class ValueType extends WebType implements Wrapper<ValueType> {

  @Override
  public ValueType as() {
    return this;
  }

}
