package com.goodow.web.core.client;

import com.goodow.web.core.client.rpc.BaseRequestTransport;
import com.goodow.web.core.shared.FileProxyStore;
import com.goodow.web.core.shared.rpc.BaseReceiver;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.RequestTransport;

public class CoreClientPlugin extends AbstractGinModule {

  public static class Startup {
    @Inject
    public Startup() {
    }
  }

  @java.lang.Override
  protected void configure() {
    bind(RequestTransport.class).to(BaseRequestTransport.class).in(Singleton.class);
    bind(FileProxyStore.class).asEagerSingleton();
    requestStaticInjection(BaseReceiver.class);
    bind(Startup.class).asEagerSingleton();
  }
}