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
package com.retech.reader.web.client.home;

import com.goodow.web.view.wave.client.panel.WavePanel;
import com.goodow.web.view.wave.client.panel.WavePanelResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SearchPanel extends WavePanel {
  interface SearchPanelUIBinder extends UiBinder<Widget, SearchPanel> {
  }

  @UiField
  TextBox searchText;
  @UiField
  HTMLPanel root;
  private static SearchPanelUIBinder uiBinder = GWT.create(SearchPanelUIBinder.class);

  @Inject
  SearchPanel() {
    Widget search = uiBinder.createAndBindUi(this);
    this.getWaveTitle().setText("搜索");
    search.addStyleName(WavePanelResources.css().waveHeader());
    this.add(search);
    searchText.getElement().setAttribute("placeholder", "搜索");
    searchText.addFocusHandler(new FocusHandler() {

      @Override
      public void onFocus(final FocusEvent arg0) {
        root.getElement().setAttribute("focused", "");
      }
    });
    searchText.addBlurHandler(new BlurHandler() {

      @Override
      public void onBlur(final BlurEvent arg0) {
        root.getElement().removeAttribute("focused");
      }
    });
  }
}
