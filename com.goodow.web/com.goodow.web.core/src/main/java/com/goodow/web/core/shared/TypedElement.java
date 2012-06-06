package com.goodow.web.core.shared;

import javax.xml.bind.annotation.XmlType;

@XmlType
public abstract class TypedElement extends NamedElement {

  protected Type type;

  private boolean many;

  public Type getType() {
    return type;
  }

  public boolean isMany() {
    return many;
  }

  public void setMany(final boolean many) {
    this.many = many;
  }

  public void setType(final Type type) {
    this.type = type;
  }

  @Override
  public EntityType type() {
    return CorePackage.TypedElement.as();
  }

}
