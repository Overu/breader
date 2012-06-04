package com.goodow.web.mvp.shared.ioc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import java.util.logging.Logger;

@Singleton
public class RfProvider<T extends RequestFactory> implements Provider<T> {
  private final RequestFactory requestFactory;
  private final Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  RfProvider(final RequestFactory requestFactory) {
    logger.finest("init begin");
    this.requestFactory = requestFactory;
    logger.finest("init end");
  }

  @Override
  @SuppressWarnings("unchecked")
  public T get() {
    return (T) requestFactory;
  }

}
