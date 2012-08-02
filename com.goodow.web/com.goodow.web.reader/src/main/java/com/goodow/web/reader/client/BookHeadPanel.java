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
package com.goodow.web.reader.client;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.reader.client.HeadTypeEvent.HeadType;
import com.goodow.web.reader.client.HeadTypeEvent.HeadTypeHandle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class BookHeadPanel extends FlowView implements HeadTypeHandle {

  public enum LocationPanel {
    Left, Right
  }

  interface Binder extends UiBinder<Widget, BookHeadPanel> {
  }

  private static Binder binder = GWT.create(Binder.class);

  private boolean isMouseMove = false;

  PopupContainer popupTopBarPanel;
  TopBarButton topBarButtonIndex;

  @UiField
  HTMLPanel leftPanel;

  @UiField
  HTMLPanel rightPanel;

  private Timer timer = new Timer() {

    @Override
    public void run() {
      popupTopBarPanel.hide();
      topBarButtonIndex.removeHover();
    }
  };

  public BookHeadPanel() {
    main.add(binder.createAndBindUi(this));
  }

  public void addTopBarButton(final TopBarButton button, final LocationPanel locationPanel) {
    switch (locationPanel) {
      case Left:
        leftPanel.add(button);
        break;
      case Right:
        rightPanel.add(button);
        break;

      default:
        break;
    }
    button.addHeadTypeHandler(this);
  }

  @Override
  public void onHeadTypeHandle(final HeadTypeEvent headTypeEvent) {
    HeadType headType = headTypeEvent.getHeadType();
    TopBarButton topBarButton = headTypeEvent.getTopBarButton();

    switch (headType) {
      case MOUSEOUT:
        if (isMouseMove) {
          return;
        }
        timer.schedule(1000);
        break;
      case CLICK:

        break;
      case MOUSEOVER:
        popupTopBarPanel.show(topBarButton);
        timer.cancel();
        topBarButton.addHover();
        if (topBarButtonIndex != null && topBarButton != topBarButtonIndex) {
          topBarButtonIndex.removeHover();
        }
        break;

      default:
        break;
    }

    topBarButtonIndex = topBarButton;
  }

  @Override
  protected void start() {
    popupTopBarPanel = new PopupContainer();
    popupTopBarPanel.addDomHandler(new MouseMoveHandler() {

      @Override
      public void onMouseMove(final MouseMoveEvent event) {
        if (isMouseMove) {
          return;
        }
        isMouseMove = true;
        timer.cancel();
      }
    }, MouseMoveEvent.getType());

    popupTopBarPanel.addDomHandler(new MouseOutHandler() {

      @Override
      public void onMouseOut(final MouseOutEvent event) {
        isMouseMove = false;
        timer.schedule(1000);
      }
    }, MouseOutEvent.getType());
  }
}
