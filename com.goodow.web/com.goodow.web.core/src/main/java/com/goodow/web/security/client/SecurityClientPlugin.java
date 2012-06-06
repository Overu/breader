package com.goodow.web.security.client;

import com.goodow.web.core.client.ClientMessage;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;


public final class SecurityClientPlugin extends SecurityClientModule {

  public static class Startup {
    @Inject
    public Startup(final ClientMessage message) {
      final Label l = new Label("hello");
      RootPanel.get().add(l);
    }
  }

  @Override
  protected void configure() {
    super.configure();
    bind(Startup.class).asEagerSingleton();
  }

}
