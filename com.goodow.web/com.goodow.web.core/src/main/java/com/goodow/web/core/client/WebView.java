package com.goodow.web.core.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;

public abstract class WebView extends Composite {

  public WebView() {
    // Execute after injection
    new Timer() {
      @Override
      public void run() {
        start();
      }
    }.schedule(1);
  }

  /**
   * Injection dependent initialization.
   */
  protected abstract void start();

}
