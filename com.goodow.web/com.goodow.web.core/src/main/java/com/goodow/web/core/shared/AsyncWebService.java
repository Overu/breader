package com.goodow.web.core.shared;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class AsyncWebService<E extends WebObject> {

  @Inject
  Provider<Message> messageProvider;

  @Inject
  WebPlatform platform;

  public ObjectType getObjectType() {
    return CorePackage.ObjectType.as();
  }

  public <T> Request<T> invoke(final Operation operation, final Object... args) {
    Message message = messageProvider.get();
    Request<T> request = message.getRequest();
    request.setTargetType(getObjectType());
    request.setOperation(operation);
    request.setArgs(args);
    return request;
  }

  public <T> Request<T> invoke(final Wrapper<Operation> operation, final Object... args) {
    return invoke(operation.as(), args);
  }
}
