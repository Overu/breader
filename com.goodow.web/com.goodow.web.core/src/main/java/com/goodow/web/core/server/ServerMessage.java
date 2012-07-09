package com.goodow.web.core.server;

import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebService;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class ServerMessage extends Message {

  @Inject
  private Injector injector;

  @Override
  public WebEntity find(final ObjectType objectType, final String id) {
    WebService<?> service = objectType.getService();
    if (service == null) {
      service = injector.getInstance(objectType.getServiceClass());
      objectType.setService(service);
    }
    return service.find(id);
  }

}
