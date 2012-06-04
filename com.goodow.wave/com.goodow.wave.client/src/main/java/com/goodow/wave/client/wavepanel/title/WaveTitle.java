/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.goodow.wave.client.wavepanel.title;

import com.goodow.wave.client.widget.button.icon.IconButtonTemplate;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlowPanel;

public class WaveTitle extends FlowPanel {

  private Element text;

  public WaveTitle() {
    this.addStyleName(WaveTitleResources.css().waveTitle());
    IconButtonTemplate iconButton = this.addIconClickButton();
    iconButton.setIconElement(AbstractImagePrototype.create(
        WaveTitleResources.image().waveTitleReturn()).createElement());
    iconButton.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        History.back();
      }
    });

    text = DOM.createSpan();
    this.getElement().appendChild(text);

    iconButton = this.addIconClickButton();
    iconButton.setIconElement(AbstractImagePrototype.create(
        WaveTitleResources.image().waveTitleClose()).createElement());
    iconButton.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        WaveTitle.this.getParent().removeFromParent();
      }
    });
  }

  public IconButtonTemplate addIconClickButton() {
    IconButtonTemplate iconButton = new IconButtonTemplate();
    iconButton.addStyleName(WaveTitleResources.css().waveTitleIcon());
    this.add(iconButton);
    return iconButton;
  }

  /**
   * 
   * set the title bar title
   */
  public void setText(final String text) {
    this.text.setInnerText(text);
  }
}
