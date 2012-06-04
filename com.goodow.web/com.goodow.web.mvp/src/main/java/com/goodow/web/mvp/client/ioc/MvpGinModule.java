package com.goodow.web.mvp.client.ioc;

import com.goodow.wave.client.shell.WaveShell;
import com.goodow.wave.client.shell.WaveShellResources.CellListResources;
import com.goodow.web.mvp.shared.BasePlace;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestParameters;


import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public final class MvpGinModule extends AbstractGinModule {
  @Singleton
  public static class Binder {
  }

  @Singleton
  public static class BinderProvider implements Provider<Binder> {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Inject
    private PlaceHistoryHandler placeHistoryHandler;

    @Override
    public Binder get() {
      logger.finest("EagerSingleton start");

      Scheduler.get().scheduleFinally(new ScheduledCommand() {
        @Override
        public void execute() {
          placeHistoryHandler.handleCurrentHistory();
        }
      });

      logger.finest("EagerSingleton end");
      return null;
    }
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    bind(Binder.class).toProvider(BinderProvider.class).asEagerSingleton();

    bind(CellList.Resources.class).to(CellListResources.class).in(Singleton.class);
    bind(WaveShell.class).asEagerSingleton();
  }

  @Provides
  @RequestParameters
  Map<String, String[]> requestParametersProvider(final PlaceController placeController) {
    Place place = placeController.getWhere();
    if (place instanceof BasePlace) {
      return ((BasePlace) place).getParams();
    }
    logger.finest("only " + BasePlace.class.getName() + " support @RequestParameters");
    return Collections.emptyMap();
  }
}
