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
package com.goodow.wave.client.wavepanel;

import com.goodow.wave.client.widget.toolbar.buttons.WaveToolBar;
import com.goodow.web.view.wave.client.title.WaveTitle;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class WavePanel extends FlowPanel {

  private WaveTitle waveTitle;

  public WavePanel() {
    addStyleName(WavePanelResources.css().wave());
  }

  public WaveToolBar addWaveToolBar() {
    WaveToolBar waveToolbar = new WaveToolBar();
    this.add(waveToolbar);
    return waveToolbar;
  }

  public WaveTitle getWaveTitle() {
    if (waveTitle == null) {
      waveTitle = new WaveTitle();
      insert(waveTitle, 0);
    }
    return waveTitle;
  }

  public void setWaveContent(final Widget content) {
    SimplePanel simplePanel = new SimplePanel();
    simplePanel.addStyleName(WavePanelResources.css().waveContent());
    simplePanel.add(content);
    this.add(simplePanel);
  }

}
