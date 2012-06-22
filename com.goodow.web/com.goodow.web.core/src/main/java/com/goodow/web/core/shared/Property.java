package com.goodow.web.core.shared;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Property extends TypedElement {

  private ObjectType declaringType;

  private boolean containment; // TODO set containment

  public ObjectType getDeclaringType() {
    return declaringType;
  }

  public boolean isContainment() {
    return containment;
  }

  public void setContainment(final boolean containment) {
    this.containment = containment;
  }

  public void setDeclaringType(final ObjectType declaringType) {
    this.declaringType = declaringType;
  }

}
