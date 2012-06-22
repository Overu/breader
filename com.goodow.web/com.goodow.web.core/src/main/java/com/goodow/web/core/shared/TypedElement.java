package com.goodow.web.core.shared;

import javax.xml.bind.annotation.XmlType;

@XmlType
public abstract class TypedElement extends NamedElement {

  protected WebType type;

  private boolean many;

  public WebType getType() {
    return type;
  }

  public boolean isMany() {
    return many;
  }

  public void setMany(final boolean many) {
    this.many = many;
  }

  public void setType(final WebType type) {
    this.type = type;
  }

}
