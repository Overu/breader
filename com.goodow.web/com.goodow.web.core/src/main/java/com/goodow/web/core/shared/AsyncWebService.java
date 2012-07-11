package com.goodow.web.core.shared;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class AsyncWebService<E extends WebObject> {

  @Inject
  Provider<Message> messageProvider;

  @Inject
  WebPlatform platform;

  public <T> Request<T> invoke(final Operation operation, final Object... args) {
    Message message = messageProvider.get();
    Request<T> request = message.getRequest();
    request.setOperation(operation);
    request.setArgs(args);
    return request;
  }

  public <T> Request<T> invoke(final Wrapper<Operation> operation, final Object... args) {
    return invoke(operation.as(), args);
  }
}
