package com.goodow.web.core.client;

import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

public abstract class ScrollView extends WebView<ScrollPanel> {

  @Override
  protected ScrollPanel createRoot() {
    return new ScrollPanel();
  }

}
