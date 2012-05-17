package com.goodow.wave.bootstrap.shared;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

import java.util.logging.Logger;

public final class BootGinModule extends AbstractGinModule {
  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    logger.finest("config start");
    bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
  }
}
