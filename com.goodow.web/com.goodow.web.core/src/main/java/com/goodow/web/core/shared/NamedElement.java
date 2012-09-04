package com.goodow.web.core.shared;

import javax.xml.bind.annotation.XmlType;

@XmlType
public abstract class NamedElement extends WebContent {

  public static final String NAME = "name";

  protected String name;

  public String getName() {
    return name;
  }

  public void setName(final String value) {
    this.name = value;
  }

  @Override
  public String toString() {
    return name;
  }
}
