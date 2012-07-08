package com.goodow.web.core.shared;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Stack;
import java.util.logging.Logger;

public abstract class RequestProcessor {

  // TODO ExceptionHandler exceptionHandler;

  public static final String SERVER_ERROR = "Server Error";

  public static final String URL = "rpc";

  public static final String JSON_CONTENT_TYPE_UTF8 = "application/json; charset=utf-8";

  protected final Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  protected Provider<Request> requestProvider;

  @Inject
  protected Provider<Response> responseProvider;

  Response<?> response;

  protected Stack<Request<?>> requests = new Stack<Request<?>>();
  protected Request<?> activeRequest;

  public boolean isActive() {
    return activeRequest != null;
  }

  public <T> Request<T> newRequest() {
    Request<?> request = requestProvider.get();
    requests.add(request);
    return (Request<T>) request;
  }

  public abstract Response send();

}
