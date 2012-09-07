package com.goodow.web.core.client;

import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public abstract class WebView<T extends Widget> extends Composite {

  protected T main;

  protected WebPlace place;

  public WebView() {
    main = createRoot();
    initWidget(main);
    // Execute after injection
    new Timer() {
      @Override
      public void run() {
        start();
      }
    }.schedule(1);
  }

  public WebPlace getPlace() {
    return place;
  }

  public boolean isScrollable() {
    return false;
  }

  public void refresh() {
  }

  public void setPlace(final WebPlace place) {
    this.place = place;
    refresh();
  }

  protected abstract T createRoot();

  protected abstract void start();

}
