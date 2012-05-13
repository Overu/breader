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
package com.goodow.web.dev.client.ui;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import org.cloudlet.web.mvp.shared.Default;
import org.cloudlet.web.mvp.shared.SimpleActivityMapper;

import java.util.logging.Logger;

public class ShellUiDev extends SimplePanel {
  private final Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  ShellUiDev(@Default final SimpleActivityMapper centerActivityMapper, final EventBus eventBus) {
    logger.finest("init start");
    ActivityManager activityManager = new ActivityManager(centerActivityMapper, eventBus);
    this.ensureDebugId("root");
    getElement().getStyle().setOverflowY(Overflow.SCROLL);
    activityManager.setDisplay(this);
    logger.finest("init end");
  }

  @Override
  public void setWidget(final IsWidget w) {
    if (w != null) {
      ensureAttached();
    }
    super.setWidget(w);
  }

  private void ensureAttached() {
    if (this.isAttached()) {
      return;
    }
    Element loading = Document.get().getElementById("loading");
    loading.removeFromParent();
    RootPanel.get().clear();
    RootPanel.get().add(this);
  }
}
