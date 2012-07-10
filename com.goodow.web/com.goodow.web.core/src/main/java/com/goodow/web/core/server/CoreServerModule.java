package com.goodow.web.core.server;

import com.goodow.web.core.shared.CorePackage;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebPlatform;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.json.JSONObject;

import java.util.logging.Logger;

public class CoreServerModule extends AbstractModule {

  @Singleton
  public static class Binder {
    @Inject
    public Binder(final JSONObjectProvider<WebObject> provider) {
      CorePackage.WebObject.as().addProvider(JSONObject.class, provider);
    }
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    logger.finest("Install JpaServiceModule begin");
    requestStaticInjection(WebPlatform.class);
    bind(Message.class).to(ServerMessage.class);
    bind(Binder.class).asEagerSingleton();
    logger.finest("Install JpaServiceModule end");
  }

}
