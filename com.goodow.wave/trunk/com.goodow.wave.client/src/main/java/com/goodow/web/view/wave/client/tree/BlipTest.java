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
package com.goodow.web.view.wave.client.tree;

import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.wavepanel.WavePanelResources;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

public class BlipTest extends WavePanel {

  @Inject
  BlipTest(final WaveBlip blip) {
    this.getWaveTitle().setText("Wave有中文版吗?");
    blip.addUserName("简泽and徐冬");
    blip.setContent("WAVE有中文的么?");
    blip.setPubTime("Aug 27, 2010");
    blip.getAuthorBox().getStyle().setDisplay(Display.BLOCK);
    blip.getEditor().getStyle().setFontWeight(FontWeight.BOLD);
    setWaveContent(blip);
    Label label = new Label("BlipTest");
    label.addStyleName(WavePanelResources.css().waveFooter());
    add(label);

  }
}
