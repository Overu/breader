package com.goodow.web.view.wave.client.toolbar;

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
