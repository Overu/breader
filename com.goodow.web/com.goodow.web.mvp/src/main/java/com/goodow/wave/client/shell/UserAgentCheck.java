package com.goodow.wave.client.shell;

import com.goodow.wave.client.wavepanel.WavePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class UserAgentCheck extends WavePanel {
  interface Binder extends UiBinder<Widget, UserAgentCheck> {
  }
  interface Style extends CssResource {
    String wavePanelRoot();
  }

  @UiField
  Style style;

  private static Binder binder = GWT.create(Binder.class);

  public UserAgentCheck() {
    this.getWaveTitle().setText("Google Wave – Your browser is not supported");
    this.setWaveContent(binder.createAndBindUi(this));
    this.addStyleName(style.wavePanelRoot());
  }
}
