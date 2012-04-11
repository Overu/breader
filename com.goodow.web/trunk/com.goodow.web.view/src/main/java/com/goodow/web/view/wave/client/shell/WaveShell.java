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
package com.goodow.web.view.wave.client.shell;

import com.goodow.web.view.wave.client.logging.WaveWarning;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.GestureStartEvent;
import com.google.gwt.event.dom.client.GestureStartHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import org.cloudlet.web.mvp.shared.Default;
import org.cloudlet.web.mvp.shared.SimpleActivityMapper;

import java.util.logging.Logger;

@Singleton
public class WaveShell extends Composite {
  interface WaveShellUiBinder extends UiBinder<Widget, WaveShell> {
  }

  private final static Logger logger = Logger.getLogger(WaveShell.class.getName());

  private static WaveShellUiBinder uiBinder = GWT.create(WaveShellUiBinder.class);
  @UiField
  SimplePanel content;
  @UiField
  FlowPanel topBar;

  static {
    WaveShellResources.css();
  }

  @Inject
  WaveShell(@Default final SimpleActivityMapper centerActivityMapper, final EventBus eventBus,
      final WaveWarning waveWarning) {
    logger.finest("init start");
    initWidget(uiBinder.createAndBindUi(this));
    topBar.add(waveWarning);
    Style style = getElement().getStyle();
    style.setOverflowY(Overflow.AUTO);
    style.setMarginTop(2, Unit.PX);
    style.setMarginLeft(7, Unit.PX);

    this.getElement().setDraggable(Element.DRAGGABLE_TRUE);
    this.addDomHandler(new GestureStartHandler() {

      @Override
      public void onGestureStart(final GestureStartEvent event) {
        History.back();
      }
    }, GestureStartEvent.getType());

    Element loading = Document.get().getElementById("loading");
    if (loading != null) {
      loading.removeFromParent();
    }
    RootPanel.get().add(this);

    ActivityManager activityManager = new ActivityManager(centerActivityMapper, eventBus);
    activityManager.setDisplay(content);

    logger.finest("init end");
  }

  public FlowPanel getTopBar() {
    return topBar;
  }

}
