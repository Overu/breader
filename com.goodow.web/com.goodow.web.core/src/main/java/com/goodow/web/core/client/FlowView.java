package com.goodow.web.core.client;

import com.google.gwt.user.client.ui.FlowPanel;

public abstract class FlowView extends WebView<FlowPanel> {

  @Override
  public boolean isScrollable() {
    return false;
  }

  @Override
  protected FlowPanel createRoot() {
    return new FlowPanel();
  }

  @Override
  protected abstract void start();

}
