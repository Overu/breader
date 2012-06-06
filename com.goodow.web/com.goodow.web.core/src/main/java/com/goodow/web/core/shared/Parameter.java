package com.goodow.web.core.shared;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Parameter extends TypedElement {

  protected Operation operation;

  public Operation getOperation() {
    return operation;
  }

  public void setOperation(final Operation operation) {
    this.operation = operation;
  }

  @Override
  public EntityType type() {
    return CorePackage.Parameter.as();
  }
}
