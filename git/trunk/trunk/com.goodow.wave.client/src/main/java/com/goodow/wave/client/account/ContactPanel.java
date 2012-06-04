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
package com.goodow.wave.client.account;

import com.goodow.wave.client.search.SearchBox;
import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.wavepanel.WavePanelResources;
import com.goodow.wave.client.wavepanel.title.WaveTitle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ContactPanel extends WavePanel {
  interface ConstactPanelUiBinder extends UiBinder<Widget, ContactPanel> {
  }
  interface Resources extends ClientBundle {
    ImageResource unknown();

    ImageResource waveContactContent();

    ImageResource waveTitleMinimize();
  }

  private static ConstactPanelUiBinder uiBinder = GWT.create(ConstactPanelUiBinder.class);
  private final static Resources res = GWT.create(Resources.class);
  private final SearchBox searchBox;
  @UiField
  HTMLPanel waveSearch;
  @UiField
  DivElement picture;
  @UiField
  HTMLPanel waveContented;
  @UiField
  HTMLPanel root;
  @UiField
  DivElement picture_Content;
  @UiField
  HTMLPanel waveFootered;
  @UiField
  FlowPanel tagLink;

  @Inject
  ContactPanel(final SearchBox searchBox) {
    this.searchBox = searchBox;
    WaveTitle waveTitle = this.getWaveTitle();
    waveTitle.setText("联系人");
    searchBox.getTextBox().getElement().setAttribute("placeholder", "查找联系人");

    uiBinder.createAndBindUi(this);
    picture.appendChild(createIcon(res.unknown()));
    root.addStyleName(WavePanelResources.css().waveHeader());
    waveSearch.add(searchBox);
    this.add(root);

    FlowPanel toDo = new FlowPanel();
    toDo.addStyleName(WavePanelResources.css().waveWarning());
    toDo.add(new HTML("<b>已完成：<b>"));
    toDo.add(new Label("11.1 部分静态界面展示"));
    toDo.add(new HTML("<br>"));
    toDo.add(new HTML("<b>待实现：<b>"));
    toDo.add(new Label("11.2 集成用户登录（难）"));
    toDo.add(new Label("11.3 界面展现（中）"));
    toDo.add(new Label("11.4 搜索联系人（中）"));
    toDo.add(new Label("11.5 实时交流（中）"));
    toDo.add(new Label("11.6 分享图书（中） "));
    add(toDo);

    waveContented.addStyleName(WavePanelResources.css().waveContent());
    picture_Content.appendChild(createIcon(res.waveContactContent()));
    this.add(waveContented);
    waveFootered.addStyleName(WavePanelResources.css().waveFooter());
    waveFootered.getElement().getStyle().setProperty("padding", "6px 5px 6px 7px");
    Hyperlink link = new Hyperlink("管理联系人", "#");
    tagLink.add(link);
    this.add(waveFootered);

  }

  public Element createIcon(final ImageResource imageResource) {
    return AbstractImagePrototype.create(imageResource).createElement();
  }

}
