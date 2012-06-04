package com.goodow.web.mvp.server;

import com.goodow.web.mvp.shared.SimplePlaceTokenizer.UrlCodex;

import com.google.inject.AbstractModule;


public class MvpModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(UrlCodex.class).to(ServerUrlCodexImpl.class);
  }

}
