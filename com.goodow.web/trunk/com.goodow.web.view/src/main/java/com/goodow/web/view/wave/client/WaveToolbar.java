package com.goodow.web.view.wave.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class WaveToolbar extends Composite {
  interface ToolbarUiBinder extends UiBinder<Widget, WaveToolbar> {
  }

  private static ToolbarUiBinder uiBinder = GWT.create(ToolbarUiBinder.class);

  public WaveToolbar() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public ToolbarClickButton addClickButton() {
    ToolbarClickButton toolbarClickButton = new ToolbarClickButton();
    getElement().appendChild(toolbarClickButton.getElement());
    return toolbarClickButton;
  }
}
