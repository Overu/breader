package com.goodow.web.reader.client;

import com.goodow.web.core.client.UIRegistry;
import com.goodow.web.reader.client.style.ReaderResources;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.logging.Logger;

public final class ReaderClientModule extends AbstractGinModule {

  @Singleton
  public static class Bind {

    public Bind() {
      ReaderResources.INSTANCE();
    }

  }

  public static class ReaderUI {
    @Inject
    public ReaderUI(final UIRegistry registry, final AsyncProvider<BookStoreView> bookstore,
        final AsyncProvider<BookShelfView> bookshelf,
        final AsyncProvider<RecommendedBookList> booklist) {
      registry.addBinding("/bookstore").toAsyncProvider(bookstore);
      registry.addBinding("/bookshelf").toAsyncProvider(bookshelf);
      registry.addBinding("/scroll").toAsyncProvider(booklist);
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
