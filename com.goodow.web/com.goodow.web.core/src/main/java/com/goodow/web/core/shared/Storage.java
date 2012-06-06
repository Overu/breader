package com.goodow.web.core.shared;

public interface Storage {

  Entity getEntity(EntityId id);

  void setEntity(Entity entity);

}
