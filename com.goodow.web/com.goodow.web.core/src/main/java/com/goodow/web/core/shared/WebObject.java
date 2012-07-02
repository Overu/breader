package com.goodow.web.core.shared;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
public abstract class WebObject implements Serializable {

  private transient ObjectType objectType;

  public Object get(final Property property) {
    return getObjectType().getAccessor().getProperty(this, property);
  }

  @XmlTransient
  public ObjectType getObjectType() {
    if (objectType == null) {
      objectType = WebPlatform.getInstance().getObjectType(getClass().getName());
    }
    return objectType;
  }

  public void set(final Property property, final Object value) {
    Accessor accessor = getObjectType().getAccessor();
    if (accessor == null) {
      System.out.println("null");
      objectType = WebPlatform.getInstance().getObjectType(getClass().getName());
    }
    accessor.setProperty(this, property, value);
  }

  public void setObjectType(final ObjectType objectType) {
    this.objectType = objectType;
  }

}
