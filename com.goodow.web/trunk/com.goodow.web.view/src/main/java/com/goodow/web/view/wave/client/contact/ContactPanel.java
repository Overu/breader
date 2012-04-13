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
package com.goodow.web.view.wave.client.contact;

import com.goodow.web.view.wave.client.panel.WavePanel;
import com.goodow.web.view.wave.client.panel.WavePanelResources;
import com.goodow.web.view.wave.client.search.SearchBox;
import com.goodow.web.view.wave.client.title.WaveTitle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ContactPanel extends WavePanel {
  interface ConstactPanelUiBinder extends UiBinder<Widget, ContactPanel> {
  }
  interface Resources extends ClientBundle {
    ImageResource unknown();

    ImageResource waveTitleMinimize();
  }

  private static ConstactPanelUiBinder uiBinder = GWT.create(ConstactPanelUiBinder.class);
  private final static Resources res = GWT.create(Resources.class);
  private final SearchBox searchBox;
  @UiField
  HTMLPanel waveSearch;
  @UiField
  DivElement picture;

  @Inject
  ContactPanel(final SearchBox searchBox) {
    this.searchBox = searchBox;
    WaveTitle waveTitle = this.getWaveTitle();
    waveTitle.setText("Contacts");
    waveTitle.addIconClickButton().setIconElement(createIcon(res.waveTitleMinimize()));
    // searchBox.addStyleName(WavePanelResources.css().waveHeader());
    searchBox.getTextBox().getElement().setAttribute("placeholder", "Search contacts");
    searchBox.getElement().getStyle().setProperty("padding", "4px 3px 0");
    Widget widget = uiBinder.createAndBindUi(this);
    picture.appendChild(createIcon(res.unknown()));
    widget.addStyleName(WavePanelResources.css().waveHeader());
    waveSearch.add(searchBox);
    this.add(widget);
  }

  public Element createIcon(final ImageResource imageResource) {
    return AbstractImagePrototype.create(imageResource).createElement();
  }

}
