package com.goodow.web.core.shared;

public interface Accessor<T> {

  Object getProperty(T entity, Property property);

  void setProperty(T entity, Property property, Object value);
}
