package com.goodow.web.mvp.client.ioc;

import com.goodow.wave.client.shell.WaveShell;
import com.goodow.wave.client.shell.WaveShellResources.CellListResources;
import com.goodow.web.core.client.rpc.BaseRequestTransport;
import com.goodow.web.feature.client.ApplicationCache;
import com.goodow.web.feature.client.Connectivity;
import com.goodow.web.feature.client.Connectivity.Listener;
import com.goodow.web.mvp.shared.BasePlace;
import com.goodow.web.mvp.shared.FileProxyStore;
import com.goodow.web.mvp.shared.tree.rpc.BaseReceiver;
import com.goodow.web.security.shared.Auth;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestParameters;
import com.google.web.bindery.requestfactory.shared.RequestTransport;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MvpGinModule extends AbstractGinModule {

  @Singleton
  public static class Binder {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Inject
    public Binder(final PlaceHistoryHandler placeHistoryHandler,
        final PlaceController placeController, @Auth final Place authRequestPlace) {
      logger.finest("EagerSingleton start");

      Scheduler.get().scheduleFinally(new ScheduledCommand() {
        @Override
        public void execute() {
          placeHistoryHandler.handleCurrentHistory();
        }
      });

      GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
        @Override
        public void onUncaughtException(final Throwable e) {
          // if (getInitCause(e) instanceof AuthorizationException) {
          // if (authRequestPlace instanceof BasePlace) {
          // ((BasePlace) authRequestPlace).setParameter(Auth.CONTINUE, Window.Location.getHref());
          // }
          // placeController.goTo(authRequestPlace);
          // return;
          // }
          logger.log(Level.WARNING, "未捕获异常", e);
        }

        private Throwable getInitCause(final Throwable e) {
          if (e != null && e.getCause() != null) {
            return getInitCause(e.getCause());
          }
          return e;
        }
      });
      Connectivity.addEventListener(new Listener() {

        @Override
        public void onOffline() {
          logger.info("网络中断");
        }

        @Override
        public void onOnline() {
          logger.info("网络恢复");
        }
      });
      ApplicationCache.addEventListener(new Command() {

        @Override
        public void execute() {
          if (GWT.isProdMode()) {// && Window.confirm("检测到新版本,是否立即升级?")) {
            Window.alert("睿泰阅读已升级至最新版本");
            Storage.getLocalStorageIfSupported().clear();
            Window.Location.reload();
          }
        }
      });

      logger.finest("EagerSingleton end");
    }
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {

    bind(RequestTransport.class).to(BaseRequestTransport.class).in(Singleton.class);
    bind(FileProxyStore.class).asEagerSingleton();
    requestStaticInjection(BaseReceiver.class);

    bind(Binder.class).asEagerSingleton();

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
