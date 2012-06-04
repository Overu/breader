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
package com.goodow.wave.client.wavepanel.blip;

import com.goodow.wave.client.account.UserStatusResources;
import com.goodow.wave.client.widget.button.icon.IconButtonTemplate;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class WaveBlip extends Composite {
  interface Binder extends UiBinder<Widget, WaveBlip> {
  }

  private static final Binder uiBinder = GWT.create(Binder.class);
  @UiField
  FlowPanel contributorPics;
  @UiField
  DivElement editor;
  @UiField
  DivElement authorName;
  @UiField
  DivElement pubTime;
  @UiField
  IconButtonTemplate moreActions;
  @UiField
  DivElement unRead;
  @UiField
  IconButtonTemplate replyPic;
  @UiField
  HTMLPanel replyPanel;
  @UiField
  DivElement dottedDivider;
  @UiField
  IconButtonTemplate popupContextIconReply;
  @UiField
  IconButtonTemplate popupContextIconEdit;
  @UiField
  DivElement popupContext;
  @UiField
  HTMLPanel root;
  @UiField
  DivElement authorBox;
  @UiField
  FlowPanel blipTree;

  public WaveBlip() {
    initWidget(uiBinder.createAndBindUi(this));
    contributorPics.addStyleName(UserStatusResources.css().waveUser());
    root.getElement().setAttribute("tabIndex", "0");
    moreActions.setIconElement(AbstractImagePrototype.create(
        WaveBlipResources.image().waveBlipMoreActions()).createElement());
    replyPic.setIconElement(AbstractImagePrototype
        .create(WaveBlipResources.image().waveBlipReply()).createElement());
    popupContextIconReply.setIconElement(AbstractImagePrototype.create(
        WaveBlipResources.image().waveBlipPopupContextReply()).createElement());
    popupContextIconEdit.setIconElement(AbstractImagePrototype.create(
        WaveBlipResources.image().waveBlipPopupContextEdit()).createElement());
    root.addDomHandler(new DoubleClickHandler() {

      @Override
      public void onDoubleClick(final DoubleClickEvent event) {
        popupContext.getStyle()
            .setOpacity(popupContext.getStyle().getOpacity().equals("0") ? 1 : 0);
      }
    }, DoubleClickEvent.getType());
  }

  public void addUserName(final String userName) {
    authorName.setInnerText(userName + ":");
  }

  public Element getAuthorBox() {
    return authorBox;
  }

  public Element getEditor() {
    return editor;
  }

  public IconButtonTemplate getEditorIcon() {
    return this.popupContextIconEdit;
  }

  public FlowPanel getUserPanel() {
    return contributorPics;
  }

  public FlowPanel getWaveBlipTree() {
    return blipTree;
  }

  public void setContent(final String html) {
    editor.setInnerHTML(html);
  }

  public void setPubTime(final String time) {
    pubTime.setInnerText(time);
  }

  public void setRead(final boolean read) {
    unRead.getStyle().setOpacity(read ? 0 : 1);
  }
}
