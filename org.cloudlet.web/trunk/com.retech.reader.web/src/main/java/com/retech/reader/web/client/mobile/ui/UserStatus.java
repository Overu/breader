package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.panel.WavePanelResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UserStatus extends Composite {
  interface Binder extends UiBinder<Widget, UserStatus> {
  }

  private Binder binder = GWT.create(Binder.class);

  public UserStatus() {
    initWidget(binder.createAndBindUi(this));
    addStyleName(WavePanelResources.css().waveHeader());
  }
}
