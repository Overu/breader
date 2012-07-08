package com.goodow.web.core.shared;

import com.google.inject.Inject;

public class AsyncWebService<E extends WebObject> {

  @Inject
  RequestProcessor processor;

  @Inject
  WebPlatform platform;

  public <T> Request<T> invoke(final Operation operation, final Object... args) {
    Request<T> request = processor.newRequest();
    request.setOperation(operation);
    request.setArgs(args);
    return request;
  }

  public <T> Request<T> invoke(final Wrapper<Operation> operation, final Object... args) {
    return invoke(operation.as(), args);
  }
}
