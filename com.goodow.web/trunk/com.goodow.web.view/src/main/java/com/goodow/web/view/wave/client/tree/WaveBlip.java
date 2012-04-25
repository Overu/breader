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

import com.goodow.web.view.wave.client.IconButtonTemplate;
import com.goodow.web.view.wave.client.contact.UserStatusResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
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
  IconButtonTemplate authorMoreActions;
  @UiField
  DivElement unRead;

  public WaveBlip() {
    initWidget(uiBinder.createAndBindUi(this));
    contributorPics.addStyleName(UserStatusResources.css().waveUser());
  }

  public void addUser(final Element element) {
    contributorPics.getElement().appendChild(element);
  }

  public void addUserName(final String userName) {
    authorName.setInnerText(userName + ":");
  }

  public IconButtonTemplate getIconButton() {
    return this.authorMoreActions;
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
