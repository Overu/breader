package com.goodow.web.core.client;

import java.io.Serializable;

import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.Operation;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.WebPlatform;
import com.goodow.web.core.shared.Wrapper;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ClientService {

  @Inject
  Provider<Message> message;

  @Inject
  WebPlatform platform;

  @Inject
  Provider<Request> requestProvider;

  public <T2 extends Serializable> Request<T2> invoke(final Operation operation,
      final Object... args) {
    Request<T2> request = requestProvider.get();
    request.setOperation(operation);
    request.setArgs(args);
    Message m = message.get();
    m.addRequest(request);
    return request;
  }

  public <T2 extends Serializable> Request<T2> invoke(final Wrapper<Operation> operation,
      final Object... args) {
    return invoke(operation.as(), args);
  }
}
