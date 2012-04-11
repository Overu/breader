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
package com.goodow.web.view.wave.client.toolbar;

import com.google.gwt.user.client.ui.FlowPanel;

public class WaveToolBar extends FlowPanel {

  public WaveToolBar() {
    addStyleName(WaveToolBarResources.css().waveToolbar());
  }

  /**
   * 
   * Create the ToolbarButton
   */
  public ToolBarClickButton addClickButton() {
    ToolBarClickButton toolbarClickButton = new ToolBarClickButton();
    if (super.getWidgetCount() == 0) {
      toolbarClickButton.setShowDivider(false);
    }
    this.add(toolbarClickButton);
    return toolbarClickButton;
  }

  /**
   * Create the ToolBarToggleButton
   */
  public ToolBarToggleButton addToggleButton() {
    return null;
  }
}
