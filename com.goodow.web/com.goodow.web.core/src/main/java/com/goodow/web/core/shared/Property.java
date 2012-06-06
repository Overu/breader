package com.goodow.web.core.shared;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Property extends TypedElement {

  private EntityType declaringType;

  private boolean containment; // TODO set containment

  public EntityType getDeclaringType() {
    return declaringType;
  }

  public boolean isContainment() {
    return containment;
  }

  public void setContainment(final boolean containment) {
    this.containment = containment;
  }

  public void setDeclaringType(final EntityType declaringType) {
    this.declaringType = declaringType;
  }

  @Override
  public EntityType type() {
    return CorePackage.Property.as();
  }

}
