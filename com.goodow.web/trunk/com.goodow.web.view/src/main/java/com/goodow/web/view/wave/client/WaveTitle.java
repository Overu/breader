package com.goodow.web.view.wave.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class WaveTitle extends Composite {

  interface WaveTitleUiBinder extends UiBinder<Widget, WaveTitle> {
  }

  private static WaveTitleUiBinder uiBinder = GWT.create(WaveTitleUiBinder.class);
  @UiField
  Element text;
  @UiField
  Element icons;

  public WaveTitle() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setText(final String text) {
    this.text.setInnerText(text);
  }
}
