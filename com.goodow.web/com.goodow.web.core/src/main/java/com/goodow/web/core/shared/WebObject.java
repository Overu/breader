package com.goodow.web.core.shared;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

@XmlType
public abstract class WebObject implements Serializable {

  public Object get(final Property property) {
    return type().getAccessor().getProperty(this, property);
  }

  public void set(final Property property, final Object value) {
    type().getAccessor().setProperty(this, property, value);
  }

  public ObjectType type() {
    // TODO
    return null;
  }
}
