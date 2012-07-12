package com.goodow.web.reader.client;

import com.goodow.web.core.shared.HomePlace;
import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import com.googlecode.mgwt.mvp.client.Animation;

import java.util.logging.Logger;

public final class ReaderMobileClientModule extends AbstractGinModule {

  @Singleton
  public static class Starter {

    @Inject
    public Starter(final ReaderPlugin ext) {
      ext.start();
    }
  }

  private static final Logger logger = Logger.getLogger(ReaderMobileClientModule.class.getName());

  @Override
  protected void configure() {
    logger.info("configure ReaderClientModule");
    bind(Starter.class).asEagerSingleton();
  }

  @Provides
  @HomePlace
  @Singleton
  WebPlace defaultPlaceProvider(final WebPlace place) {
    place.setAnimation(Animation.FADE);
    place.setTitle("睿泰书城");
    place.setPath("");
    return place;
  }
}