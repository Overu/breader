package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.ReaderPlace;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.logging.Logger;

public final class ReaderMobileClientModule extends AbstractGinModule {

  @Singleton
  public static class Starter {

    @Inject
    public Starter(final ReaderPlace reader) {
    }
  }

  private static final Logger logger = Logger.getLogger(ReaderMobileClientModule.class.getName());

  @Override
  protected void configure() {
    logger.info("configure ReaderClientModule");
    bind(Starter.class).asEagerSingleton();
  }

}
