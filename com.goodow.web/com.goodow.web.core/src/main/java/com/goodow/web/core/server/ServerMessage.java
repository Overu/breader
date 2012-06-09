package com.goodow.web.core.server;

import com.goodow.web.core.shared.Entity;
import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.EntityType;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.Response;
import com.goodow.web.core.shared.Service;
import com.goodow.web.core.shared.WebPlatform;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class ServerMessage extends Message {

  @Inject
  private WebPlatform platform;

  @Inject
  private Injector injector;

  @Override
  public Entity find(final EntityId id) {
    EntityType entityType = platform.getEntityType(id.getEntityType());
    if (id.getStableId() != null) {
      Service s = entityType.getService();
      if (s == null) {
        s = injector.getInstance(entityType.getServiceClass());
      }
      return (Entity) s.find(id.getStableId());
    } else {
      return entityType.create();
    }
  }

  @Override
  public Response send() {
    return null;
  }
}
