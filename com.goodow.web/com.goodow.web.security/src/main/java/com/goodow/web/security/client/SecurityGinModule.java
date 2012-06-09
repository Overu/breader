package com.goodow.web.security.client;

import com.goodow.web.feature.client.ApplicationCache;
import com.goodow.web.feature.client.Connectivity;
import com.goodow.web.feature.client.Connectivity.Listener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.shiro.authz.AuthorizationException;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class SecurityGinModule extends AbstractGinModule {

  @Singleton
  public static class Binder {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Inject
    public Binder() {
      logger.finest("SecurityGinModule start");

      GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
        @Override
        public void onUncaughtException(final Throwable e) {
          if (getInitCause(e) instanceof AuthorizationException) {
            logger.log(Level.WARNING, "AuthorizationException", e);
          }
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

  @Override
  protected void configure() {
    bind(Binder.class).asEagerSingleton();
  }
}
