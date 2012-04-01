package com.goodow.web.view.wave.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlowPanel;

public class WaveTitle extends FlowPanel {
  interface Resources extends ClientBundle {
    @Source("waveTitle.css")
    Style style();

    ImageResource waveTitleReturn();
  }
  interface Style extends CssResource {
    String waveTitle();

    String waveTitleText();
  }

  private static final Resources res = GWT.create(Resources.class);
  static {
    res.style().ensureInjected();
  }

  public WaveTitle() {
    this.addStyleName(res.style().waveTitle());
    IconButtonTemplate iconButton = this.addIconClickButton();
    iconButton.setIconElement(AbstractImagePrototype.create(res.waveTitleReturn()).createElement());
    iconButton.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        History.back();
      }
    });
  }

  public IconButtonTemplate addIconClickButton() {
    IconButtonTemplate iconButton = new IconButtonTemplate();
    this.add(iconButton);
    return iconButton;
  }

  /**
   * 
   * set the title bar title
   */
  public void setText(final String text) {
    Element element = DOM.createDiv();
    element.setInnerText(text);
    element.addClassName(res.style().waveTitleText());
    this.getElement().appendChild(element);
  }
}
