package com.goodow.web.reader.client;

import com.goodow.web.core.client.UIRegistry;
import com.goodow.web.reader.client.style.ReadResources;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.logging.Logger;

public final class ReaderClientModule extends AbstractGinModule {

  @Singleton
  public static class Bind {

    public Bind() {
      ReadResources.INSTANCE();
    }

  }

  public static class ReaderUI {
    @Inject
    public ReaderUI(final UIRegistry registry, final AsyncProvider<BookStoreView> bookstore,
        final AsyncProvider<BookShelfView> bookshelf) {
      registry.bind("/bookstore").toAsyncProvider(bookstore);
      registry.bind("/bookshelf").toAsyncProvider(bookshelf);
    }
  }

  private static final Logger logger = Logger.getLogger(ReaderClientModule.class.getName());

  @Override
  protected void configure() {
    logger.info("configure ReaderClientModule");
    bind(ReaderUI.class).asEagerSingleton();
    bind(Bind.class).asEagerSingleton();
  }

}
