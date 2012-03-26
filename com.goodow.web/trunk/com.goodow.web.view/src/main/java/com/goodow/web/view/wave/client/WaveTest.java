package com.goodow.web.view.wave.client;

import com.google.gwt.user.client.ui.Label;

public class WaveTest extends WavePanel {

  public WaveTest() {
    title().setText("test title");
    // wave.setHeader(new Label("test header"));
    setContent(new Label("test content"));
    ToolbarClickButton btn = toolbar().addClickButton();
    btn.setText("test");
    // wave.setFooter(new Label("test footer"));
    // ToolbarClickButton clickButton = wave.toolbar().addClickButton();
    // clickButton.setText("test button");

  }

}
