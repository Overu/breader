package com.goodow.wave.bootstrap.client;

import com.google.gwt.inject.client.AbstractGinModule;

import java.util.logging.Logger;

final class BootGinModule extends AbstractGinModule {
  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    logger.finest("config start");
    install(new com.goodow.wave.bootstrap.shared.BootGinModule());
  }
}
