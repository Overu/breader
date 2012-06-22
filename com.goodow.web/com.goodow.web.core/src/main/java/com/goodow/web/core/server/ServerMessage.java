package com.goodow.web.core.server;

import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.ObjectType;
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
  public WebEntity find(final EntityId id) {
    ObjectType entityType = platform.getEntityType(id.getEntityType());
    if (id.getStableId() != null) {
      Service s = entityType.getService();
      if (s == null) {
        s = injector.getInstance(entityType.getServiceClass());
      }
      return (WebEntity) s.find(id.getStableId());
    } else {
      return (WebEntity) entityType.create();
    }
  }

  @Override
  public Response send() {
    return null;
  }
}
