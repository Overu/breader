package org.cloudlet.web.boot.client;

import com.google.gwt.inject.client.AbstractGinModule;

import java.util.logging.Logger;

final class BootGinModule extends AbstractGinModule {
  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    logger.finest("config start");
    install(new org.cloudlet.web.boot.shared.BootGinModule());
  }
}
