package com.goodow.web.view.wave.client.ioc;

import com.goodow.web.view.wave.client.shell.WaveBundle.CellListResources;
import com.goodow.web.view.wave.client.shell.WaveShell;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.user.cellview.client.CellList;
import com.google.inject.Singleton;

import java.util.logging.Logger;

public final class WaveGinModule extends AbstractGinModule {

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    bind(CellList.Resources.class).to(CellListResources.class).in(Singleton.class);
    bind(WaveShell.class).asEagerSingleton();
  }

}
