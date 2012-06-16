package com.goodow.web.security.shared;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.logging.Logger;

public final class SecuritySharedModule extends AbstractGinModule {

  @Singleton
  public static class Binder {
    @Inject
    public Binder() {

      logger.finest("SecuritySharedModule start");

      logger.finest("EagerSingleton end");
    }
  }

  private static final Logger logger = Logger.getLogger(SecuritySharedModule.class.getName());

  @Override
  protected void configure() {
    bind(Binder.class).asEagerSingleton();
  }
}
