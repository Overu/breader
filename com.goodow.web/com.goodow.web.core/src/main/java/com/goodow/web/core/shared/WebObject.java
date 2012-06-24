package com.goodow.web.core.shared;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

@XmlType
public abstract class WebObject implements Serializable {

  private ObjectType objectType;

  public Object get(final Property property) {
    return getObjectType().getAccessor().getProperty(this, property);
  }

  public ObjectType getObjectType() {
    if (objectType == null) {
      objectType = WebPlatform.getInstance().getEntityType(getClass().getName());
    }
    return objectType;
  }

  public void set(final Property property, final Object value) {
    getObjectType().getAccessor().setProperty(this, property, value);
  }

  public void setObjectType(final ObjectType objectType) {
    this.objectType = objectType;
  }

}
