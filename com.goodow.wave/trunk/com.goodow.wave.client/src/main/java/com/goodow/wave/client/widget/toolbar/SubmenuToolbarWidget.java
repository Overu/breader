package com.goodow.wave.client.widget.toolbar;

import com.goodow.wave.client.widget.toolbar.buttons.VerticalToolbarButtonWidget;
import com.goodow.wave.client.widget.toolbar.buttons.WaveToolBarResources;

import com.google.gwt.user.client.ui.FlowPanel;

public class SubmenuToolbarWidget extends FlowPanel {
  public SubmenuToolbarWidget() {
    addStyleName(WaveToolBarResources.css().submenuToolbarWidget());
  }

  public VerticalToolbarButtonWidget addClickButton() {
    VerticalToolbarButtonWidget btn = new VerticalToolbarButtonWidget();
    add(btn);
    return btn;
  }
}
