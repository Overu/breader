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
package com.goodow.web.ui.client.nav;

import com.goodow.web.mvp.shared.ActivityAware;
import com.goodow.web.mvp.shared.ActivityState;
import com.goodow.web.mvp.shared.layout.Nav;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;


import java.util.logging.Logger;

@Singleton
public class NavUi extends Composite implements ActivityAware {
  private FlowPanel flowPanel = new FlowPanel();
  private ScrollPanel layout = new ScrollPanel(flowPanel);
  private final Logger logger = Logger.getLogger(getClass().getName());
  private final TopNavUi top;
  private final TagsUi tags;
  private static int PX = 7;

  @Inject
  NavUi(final TopNavUi top, final TagsUi tags) {
    logger.finest("init start");
    this.top = top;
    this.tags = tags;

    DecoratorPanel panel = new DecoratorPanel();
    panel.setWidget(top);
    // flowPanel.add(panel);
    DecoratorPanel panel2 = new DecoratorPanel();
    panel2.setWidget(tags);
    flowPanel.add(panel2);

    top.setWidth(Nav.WIDTH - PX * 2 - 10 + Unit.PX.getType());
    tags.setWidth(Nav.WIDTH - PX * 2 - 10 + Unit.PX.getType());
    layout.getElement().getStyle().setPadding(PX, Unit.PX);
    // tags.getElement().getStyle().setBorderColor("#E5E5E5");
    // tags.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
    // tags.getElement().getStyle().setBorderWidth(1, Unit.PX);
    initWidget(layout);
  }

  @Override
  public void onStart(final ActivityState state) {
    top.onStart(state);
    tags.onStart(state);
  }
}
