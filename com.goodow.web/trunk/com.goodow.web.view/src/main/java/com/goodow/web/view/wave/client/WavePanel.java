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
package com.goodow.web.view.wave.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class WavePanel extends FlowPanel {

  interface Bundle extends ClientBundle {
    @Source("WavePanel.css")
    Style style();
  }

  interface Style extends CssResource {
    String wave();

    String waveContent();

    String waveWarning();
  }

  private static Bundle BUNDLE = GWT.create(Bundle.class);
  static {
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
