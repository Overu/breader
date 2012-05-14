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

import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.widget.toolbar.buttons.ToolBarClickButton;
import com.goodow.wave.client.widget.toolbar.buttons.WaveToolBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import org.cloudlet.web.mvp.shared.BasePlace;

@Singleton
public class TopBar extends WavePanel {

  interface Resources extends ClientBundle {

    ImageResource refresh();

    ImageResource search();

    ImageResource settings();

    ImageResource share();
  }

  private static Resources res = GWT.create(Resources.class);

  @Inject
  public TopBar(final PlaceController placeController, final Provider<BasePlace> places) {
    WaveToolBar toolbar = this.addWaveToolBar();
    ToolBarClickButton index = toolbar.addClickButton();
    index.setText("首页");
    index.setVisualElement(createIcon(res.settings()));

    ToolBarClickButton myDownload = toolbar.addClickButton();
    myDownload.setText("下载管理");
    myDownload.setVisualElement(createIcon(res.settings()));

    ToolBarClickButton before = toolbar.addClickButton();
    before.setText("前30本");
    before.setVisualElement(createIcon(res.settings()));

    ToolBarClickButton all = toolbar.addClickButton();
    all.setText("全部");
    all.setVisualElement(createIcon(res.settings()));

    ToolBarClickButton edit = toolbar.addClickButton();
    edit.setText("编辑");
    edit.setVisualElement(createIcon(res.settings()));

    index.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        placeController.goTo(places.get().setPath("/"));
      }
    });
  }

  public Element createIcon(final ImageResource imageResource) {
    return AbstractImagePrototype.create(imageResource).createElement();
  }

}
