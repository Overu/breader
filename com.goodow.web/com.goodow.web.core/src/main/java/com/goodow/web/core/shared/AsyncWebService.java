package com.goodow.web.core.shared;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class AsyncWebService<E extends WebObject> {

  @Inject
  Provider<Message> message;

  @Inject
  WebPlatform platform;

  @Inject
  Provider<Request> requestProvider;

  public <T> Request<T> invoke(final Operation operation, final Object... args) {
    Request<T> request = requestProvider.get();
    request.setOperation(operation);
    request.setArgs(args);
    Message m = message.get();
    m.addRequest(request);
    return request;
  }

  public <T> Request<T> invoke(final Wrapper<Operation> operation, final Object... args) {
    return invoke(operation.as(), args);
  }
}
