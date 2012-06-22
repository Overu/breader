package com.goodow.web.core.shared;

public interface Storage {

  WebObject getEntity(EntityId id);

  void setEntity(WebObject entity);

}
