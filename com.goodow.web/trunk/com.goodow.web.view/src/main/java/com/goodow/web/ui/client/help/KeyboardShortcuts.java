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
package com.goodow.web.ui.client.help;

import com.goodow.web.ui.client.style.WebAppResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import java.util.logging.Logger;

@Singleton
public class KeyboardShortcuts extends Composite {
  interface KeyboardShortcutsUiBinder extends UiBinder<Widget, KeyboardShortcuts> {
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final DecoratedPopupPanel popup;

  private static KeyboardShortcutsUiBinder uiBinder = GWT.create(KeyboardShortcutsUiBinder.class);
  @UiField
  Label close;

  public KeyboardShortcuts() {
    popup = new DecoratedPopupPanel(true);
    popup.setStyleName(WebAppResources.styles().keyboardShortcuts());
    initWidget(uiBinder.createAndBindUi(this));

    setSize("800px", "600px");
    popup.setWidget(this);
  }

  public void toggleVisible() {
    if (popup.isShowing()) {
      logger.info("隐藏键盘快捷键帮助(?)");
      popup.hide();
    } else {
      popup.center();
      logger.info("显示键盘快捷键帮助(?)");
      popup.show();
    }
  }

  @UiHandler("close")
  void onCloseClick(final ClickEvent event) {
    popup.hide();
  }

}
