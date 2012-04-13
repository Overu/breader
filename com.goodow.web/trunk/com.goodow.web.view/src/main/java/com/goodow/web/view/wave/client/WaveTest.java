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

import com.goodow.web.view.wave.client.panel.WavePanel;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;

public class WaveTest extends WavePanel {

  public WaveTest() {
    // getWaveTitle().setText("test title");
    // wave.setHeader(new Label("test header"));
    // setWaveContent(new Label("test content"));
    // ToolBarClickButton btn = addWaveToolBar().addClickButton();
    // btn.setText("test");
    // wave.setFooter(new Label("test footer"));
    // ToolBarClickButton clickButton = wave.toolbar().addClickButton();
    // clickButton.setText("test button");
    HTMLPanel hp = new HTMLPanel("");
    hp.setWidth("100%");
    hp.setHeight("700px");
    setWaveContent(hp);

    hp.addDomHandler(new TouchStartHandler() {

      @Override
      public void onTouchStart(final TouchStartEvent event) {
        JsArray<Touch> touches = event.getTouches();
        if (touches.length() == 2) {
          Window.alert("sdfsafdsadf");
        }
      }
    }, TouchStartEvent.getType());

  }
}
