package com.goodow.web.core.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public abstract class WebView extends Composite {

  protected FlowPanel main;

  public WebView() {
    main = new FlowPanel();
    initWidget(main);
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
