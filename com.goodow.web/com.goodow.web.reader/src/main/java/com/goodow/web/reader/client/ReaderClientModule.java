package com.goodow.web.reader.client;

import com.goodow.web.core.client.TextResourceEditor;
import com.goodow.web.core.client.UIManager;
import com.goodow.web.core.shared.HomePlace;
import com.goodow.web.core.shared.WebPlace;
import com.goodow.web.reader.client.style.ReadResources;
import com.goodow.web.reader.client.style.ReadResources.CellListResources;
import com.goodow.web.reader.shared.ReaderPlace;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.user.cellview.client.CellList;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import java.util.logging.Logger;

public final class ReaderClientModule extends AbstractGinModule {

  @Singleton
  public static class Bind {
    @Inject
    public Bind(final Provider<TextResourceEditor> textEditor, final UIManager registry) {
      ReadResources.INSTANCE();
      registry.bind("text/plain").toProvider(textEditor);
      registry.bind("application/xhtml+xml").toProvider(textEditor);
    }
  }

  private static final Logger logger = Logger.getLogger(ReaderClientModule.class.getName());

  @Override
  protected void configure() {
    logger.info("configure ReaderClientModule");
    bind(Bind.class).asEagerSingleton();
    bind(CellList.Resources.class).to(CellListResources.class).in(Singleton.class);
    bind(WebPlace.class).annotatedWith(HomePlace.class).to(ReaderPlace.class);
  }

}
