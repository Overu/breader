package com.goodow.web.core.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Inject;

public class CoreClientPlugin extends AbstractGinModule {

  public static class Startup {
    @Inject
    public Startup() {
    }
  }

  @java.lang.Override
  protected void configure() {
    bind(Startup.class).asEagerSingleton();
  }
}