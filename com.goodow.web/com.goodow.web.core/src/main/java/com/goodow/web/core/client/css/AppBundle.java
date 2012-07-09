package com.goodow.web.core.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface AppBundle extends ClientBundle {
  public static final AppBundle INSTANCE = GWT.create(AppBundle.class);

  ImageResource a();

  ImageResource b();

  @Source("bottonCss.css")
  ReadButtonCss buttonCss();

  @Source("carouseCss.css")
  ReadCarouseCss carouseCss();

  // This is a very nasty workaround because GWT CssResource does not support @media correctly!
  @Source("app.css")
  TextResource css();

}
