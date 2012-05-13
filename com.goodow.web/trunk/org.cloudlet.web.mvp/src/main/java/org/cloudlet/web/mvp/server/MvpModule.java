package org.cloudlet.web.mvp.server;

import com.google.inject.AbstractModule;

import org.cloudlet.web.mvp.shared.SimplePlaceTokenizer.UrlCodex;

public class MvpModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(UrlCodex.class).to(ServerUrlCodexImpl.class);
  }

}
