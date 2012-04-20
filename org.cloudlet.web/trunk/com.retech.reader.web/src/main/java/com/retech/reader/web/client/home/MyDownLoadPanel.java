/*
 * C Copyright 2012 Goodow.com
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MyDownLoadPanel extends WavePanel {

  interface Binder extends UiBinder<Widget, MyDownLoadPanel> {
  }

  interface Resources extends ClientBundle {
    ImageResource addIssue();
  }

  private static Resources res = GWT.create(Resources.class);
  private static Binder binder = GWT.create(Binder.class);

  @UiField
  FlowPanel myDownLoadPanel;

  MyDownLoadPanel() {
    this.getWaveTitle().setText("我的下载");
    this.setWaveContent(binder.createAndBindUi(this));

    HTMLPanel downLoad = new HTMLPanel("");
    HTMLPanel imagePanel = new HTMLPanel(AbstractImagePrototype.create(res.addIssue()).getHTML());
    imagePanel.getElement().getStyle().setOpacity(0);
    downLoad.add(imagePanel);
    downLoad.add(new Label("下载"));
    downLoad.getElement().getStyle().setOpacity(0);
    myDownLoadPanel.getElement().getStyle().setCursor(Cursor.POINTER);
    myDownLoadPanel.add(downLoad);
  }
}
