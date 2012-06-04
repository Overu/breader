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
package com.goodow.web.ui.client.shell.one;

import com.goodow.web.mvp.shared.Default;
import com.goodow.web.mvp.shared.SimpleActivityMapper;
import com.goodow.web.mvp.shared.layout.Footer;
import com.goodow.web.mvp.shared.layout.Nav;
import com.goodow.web.mvp.shared.layout.Search;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;


import java.util.logging.Logger;

/**
 * Application shell.
 */
@Singleton
public final class OnePageShell extends Composite {

  interface ShellUiBinder extends UiBinder<Widget, OnePageShell> {
  }

  private static ShellUiBinder uiBinder = GWT.create(ShellUiBinder.class);

  private final Logger logger = Logger.getLogger(getClass().getName());

  @UiField
  HTMLPanel root;

  @UiField
  ActivityPanel center;

  @UiField
  ActivityPanel search;

  @UiField
  ActivityPanel nav;

  @UiField
  ActivityPanel footer;

  private final Provider<ActivityPanel> activityPanelProvider;

  @Inject
  public OnePageShell(final Provider<ActivityPanel> activityPanelProvider,
      @Default final SimpleActivityMapper centerActivityMapper,
      @Search final SimpleActivityMapper searchActivityMapper,
      @Nav final SimpleActivityMapper navActivityMapper,
      @Footer final SimpleActivityMapper footerActivityMapper, final EventBus eventBus) {
    this.activityPanelProvider = activityPanelProvider;
    logger.finest("init start");

    // Initialize the ui binder.
    uiBinder = GWT.create(ShellUiBinder.class);
    initWidget(uiBinder.createAndBindUi(this));

    ActivityManager searchActivityManager = new ActivityManager(searchActivityMapper, eventBus);
    search.ensureDebugId("search");
    searchActivityManager.setDisplay(search);

    ActivityManager leftNavActivityManager = new ActivityManager(navActivityMapper, eventBus);
    nav.ensureDebugId("leftNav");
    leftNavActivityManager.setDisplay(nav);

    ActivityManager footerActivityManager = new ActivityManager(footerActivityMapper, eventBus);
    footer.ensureDebugId("footer");
    footerActivityManager.setDisplay(footer);

    ActivityManager activityManager = new ActivityManager(centerActivityMapper, eventBus);
    center.ensureDebugId("contentPanel");
    activityManager.setDisplay(center);

    logger.finest("init end");
  }

  @UiFactory
  ActivityPanel activityPanelProvider() {
    return activityPanelProvider.get();
  }

}
