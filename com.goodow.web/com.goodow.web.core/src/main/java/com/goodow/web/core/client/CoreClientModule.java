package com.goodow.web.core.client;

import com.goodow.web.core.shared.WebPlatform;

import com.google.gwt.inject.client.AbstractGinModule;

public class CoreClientModule extends AbstractGinModule {

  @java.lang.Override
  protected void configure() {
    requestStaticInjection(WebPlatform.class);
  }
}