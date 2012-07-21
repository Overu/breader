package com.goodow.web.core.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface AppBundle extends ClientBundle {

  interface Style extends CssResource {
    String fullScreenStyle();

    String webKitFlex();
  }

  public static final AppBundle INSTANCE = GWT.create(AppBundle.class);

  // This is a very nasty workaround because GWT CssResource does not support @resource correctly!
  @Source("app.css")
  Style css();
}
