package com.goodow.web.reader.client;

import com.goodow.web.reader.client.style.ReadResources;
import com.goodow.web.reader.client.style.ReadResources.CellListResources;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.user.cellview.client.CellList;
import com.google.inject.Singleton;

import java.util.logging.Logger;

public final class ReaderClientModule extends AbstractGinModule {

  @Singleton
  public static class Bind {
    public Bind() {
      ReadResources.INSTANCE();
    }
  }

  private static final Logger logger = Logger.getLogger(ReaderClientModule.class.getName());

  @Override
  protected void configure() {
    logger.info("configure ReaderClientModule");
    bind(Bind.class).asEagerSingleton();
    bind(CellList.Resources.class).to(CellListResources.class).in(Singleton.class);
  }

}
