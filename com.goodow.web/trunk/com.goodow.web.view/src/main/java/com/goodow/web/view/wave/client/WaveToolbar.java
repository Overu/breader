package com.goodow.web.view.wave.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.client.ui.FlowPanel;

public class WaveToolbar extends FlowPanel {
  interface Resources extends ClientBundle {
    @Source("WaveToolbar.css")
    Style style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource waveToolbarActive();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource waveToolbarEmpty();

  }
  interface Style extends CssResource {
    String waveToolbar();
  }

  static final Resources res = GWT.create(Resources.class);
  static {
    res.style().ensureInjected();
  }

  public WaveToolbar() {
    addStyleName(res.style().waveToolbar());
  }

  /**
   * 
   * Create the ToolbarButton
   */
  public ToolbarClickButton addClickButton() {
    ToolbarClickButton toolbarClickButton = new ToolbarClickButton();
    this.add(toolbarClickButton);
    return toolbarClickButton;
  }

  /**
   * Create the ToolbarToggleButton
   */
  public ToolbarToggleButton addToggleButton() {
    return null;
  }
}
