package com.goodow.web.view.wave.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class WavePanel extends FlowPanel {

  interface Bundle extends ClientBundle {
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource bodyBackgroundTop();

    @NotStrict
    @Source("waveClean.css")
    CssResource clean();

    @Source("WavePanel.css")
    Style style();
  }

  interface Style extends CssResource {
    String wave();

    String waveContent();
  }

  private static Bundle BUNDLE = GWT.create(Bundle.class);
  static {
    BUNDLE.clean().ensureInjected();
    BUNDLE.style().ensureInjected();
  }
  private WaveTitle waveTitle;

  public WavePanel() {
    addStyleName(BUNDLE.style().wave());
  }

  public void setContent(final Widget content) {
    SimplePanel simplePanel = new SimplePanel();
    simplePanel.addStyleName(BUNDLE.style().waveContent());
    simplePanel.add(content);
    this.add(simplePanel);
  }

  public WaveTitle title() {
    if (waveTitle == null) {
      waveTitle = new WaveTitle();
      insert(waveTitle, 0);
    }
    return waveTitle;
  }

  public WaveToolbar toolbar() {
    WaveToolbar waveToolbar = new WaveToolbar();
    this.add(waveToolbar);
    return waveToolbar;
  }

}
