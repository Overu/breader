package com.goodow.web.reader.client;

import com.goodow.web.core.client.UIRegistry;
import com.goodow.web.core.shared.HomePlace;
import com.goodow.web.core.shared.MyPlace;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import com.googlecode.mgwt.mvp.client.Animation;

import java.util.logging.Logger;

public final class ReaderMobileClientModule extends AbstractGinModule {

  public static class ReaderUI {
    @Inject
    public ReaderUI(final UIRegistry registry, final AsyncProvider<BookStoreView> bookstore,
        final AsyncProvider<BookShelfView> bookshelf) {
      registry.bind("/bookstore").toAsyncProvider(bookstore);
      registry.bind("/bookshelf").toAsyncProvider(bookshelf);
    }
  }

  private static final Logger logger = Logger.getLogger(ReaderMobileClientModule.class.getName());

  @Override
  protected void configure() {
    logger.info("configure ReaderClientModule");
    bind(ReaderUI.class).asEagerSingleton();
  }

  @Provides
  @HomePlace
  @Singleton
  MyPlace defaultPlaceProvider(final MyPlace place) {
    place.setAnimation(Animation.FADE);
    place.setTitle("睿泰书城");
    place.setUri("/bookstore");
    return place;
  }
}
