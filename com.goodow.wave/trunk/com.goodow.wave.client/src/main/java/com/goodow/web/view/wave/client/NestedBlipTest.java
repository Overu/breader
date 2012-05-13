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

import com.goodow.wave.client.account.UserStatusResources;
import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.wavepanel.WavePanelResources;
import com.goodow.wave.client.widget.button.icon.IconButtonTemplate;
import com.goodow.wave.client.widget.toolbar.buttons.ToolBarButtonView.State;
import com.goodow.web.view.wave.client.tree.WaveBlip;
import com.goodow.web.view.wave.client.tree.WaveBlipTree;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

public class NestedBlipTest extends WavePanel {

  @Inject
  public NestedBlipTest(WaveBlip blip, final FlowPanel flowPanel, final WaveBlipTree blipTree) {
    this.getWaveTitle().setText("Wave有中文版吗?");
    blip.addUserName("简泽and徐冬");
    blip.setContent("WAVE有中文的么?");
    blip.setPubTime("Aug 27, 2010");
    blip.getAuthorBox().getStyle().setDisplay(Display.BLOCK);
    blip.getEditor().getStyle().setFontWeight(FontWeight.BOLD);
    flowPanel.add(blip);
    FlowPanel flowPanel2 = blip.getWaveBlipTree();
    blip = new WaveBlip();
    blip.getUserPanel().getElement().appendChild(
        createIcon(UserStatusResources.image().waveUserGroup()));
    blip.getUserPanel().addStyleName(UserStatusResources.css().waveUserGroup());
    blip.setContent("没有吧，不太适合国人");
    blip.addUserName("Himcax@googlewave.com");
    blip.setPubTime("Oct 27, 2010");
    blipTree.setWaveBlip(blip);
    blip = new WaveBlip();
    blip.setRead(false);
    blip.getUserPanel().getElement().appendChild(
        createIcon(UserStatusResources.image().waveUserGroup()));
    blip.getUserPanel().addStyleName(UserStatusResources.css().waveUserAvailable());
    blip.setContent("现在看见英文就头疼");
    blip.addUserName("Cholse sun");
    blip.setPubTime("Nov 24, 2011");
    blipTree.setWaveBlip(blip);
    flowPanel2.add(blipTree);
    IconButtonTemplate editorIcon = blip.getEditorIcon();
    editorIcon.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        Window.alert("编辑图标");
      }
    });
    editorIcon.setState(State.DISABLED);
    blip = new WaveBlip();
    blip.setContent("真的很难用");
    blip.setPubTime("Jul 22, 2010");
    blip.addUserName("Cathy Shen");
    blip.getUserPanel().getElement().appendChild(
        createIcon(UserStatusResources.image().waveUserGroup()));
    blip.getUserPanel().addStyleName(UserStatusResources.css().waveUserAvailable());
    blip.setRead(true);
    flowPanel.add(blip);
    setWaveContent(flowPanel);
    Label label = new Label("Test");
    label.setStyleName(WavePanelResources.css().waveFooter());
    this.add(label);

  }

  public Element createIcon(final ImageResource imageResource) {
    return AbstractImagePrototype.create(imageResource).createElement();
  }

}
