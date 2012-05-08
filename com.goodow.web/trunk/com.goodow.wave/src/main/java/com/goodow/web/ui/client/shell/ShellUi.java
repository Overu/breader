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
package com.goodow.web.ui.client.shell;

import com.goodow.web.ui.client.help.KeyboardShortcuts;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import org.cloudlet.web.mvp.shared.Default;
import org.cloudlet.web.mvp.shared.SimpleActivityMapper;
import org.cloudlet.web.mvp.shared.layout.Footer;
import org.cloudlet.web.mvp.shared.layout.Nav;
import org.cloudlet.web.mvp.shared.layout.Search;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application shell.
 */
@Singleton
public final class ShellUi extends ResizeComposite {

  interface ShellUiBinder extends UiBinder<Widget, ShellUi> {
  }

  private static ShellUiBinder uiBinder = GWT.create(ShellUiBinder.class);
  private final Logger logger = Logger.getLogger(getClass().getName());
  private static int ANIMATE_DURATION = 200;
  // 仅用于取消全屏时判断是否显示
  private boolean hasSearch;
  private boolean hasNav;
  private boolean hasFooter;
  private boolean isFullScreen;

  @UiField
  DockLayoutPanel rootLayout;

  /**
   * The panel that holds the content.
   */
  @UiField(provided = true)
  SimpleLayoutPanel center;

  @UiField(provided = true)
  SimplePanel search;

  @UiField(provided = true)
  SimplePanel nav;

  @UiField(provided = true)
  SimplePanel footer;

  private final AsyncProvider<KeyboardShortcuts> keyboardShortcutsProvider;
  private final EventBus eventBus;
  private final SimpleActivityMapper searchActivityMapper;
  private final SimpleActivityMapper navActivityMapper;
  private final SimpleActivityMapper footerActivityMapper;

  /**
   * Construct the {@link ShellUi}.
   * 
   * @param treeModel the treeModel that backs the main menu
   */
  @Inject
  public ShellUi(@Default final SimpleActivityMapper centerActivityMapper,
      @Search final SimpleActivityMapper searchActivityMapper,
      @Nav final SimpleActivityMapper navActivityMapper,
      @Footer final SimpleActivityMapper footerActivityMapper, final EventBus eventBus,
      final AsyncProvider<KeyboardShortcuts> keyboardShortcutsProvider) {
    this.searchActivityMapper = searchActivityMapper;
    this.navActivityMapper = navActivityMapper;
    this.footerActivityMapper = footerActivityMapper;
    logger.finest("init start");

    this.eventBus = eventBus;
    this.keyboardShortcutsProvider = keyboardShortcutsProvider;

    center = new SimpleLayoutPanel() {
      @Override
      public void setWidget(final IsWidget w) {
        ensureAttached(w);
        super.setWidget(w);
      };
    };
    ActivityManager activityManager = new ActivityManager(centerActivityMapper, eventBus);
    center.ensureDebugId("center");
    activityManager.setDisplay(center);
    createNullableLayout();

    // Initialize the ui binder.
    initWidget(uiBinder.createAndBindUi(this));
    addKeyboardShortcutsSupport();

    logger.finest("init end");
  }

  private void addKeyboardShortcutsSupport() {
    NativePreviewHandler handler = new NativePreviewHandler() {

      @Override
      public void onPreviewNativeEvent(final NativePreviewEvent event) {

        int typeInt = event.getTypeInt();
        if (typeInt != Event.ONKEYPRESS) {
          return;
        }

        NativeEvent nativeEvent = event.getNativeEvent();
        EventTarget eventTarget = nativeEvent.getEventTarget();
        Element target = Element.as(eventTarget);
        String tagName = target.getTagName();
        if ("input".equalsIgnoreCase(tagName) || "textarea".equalsIgnoreCase(tagName)) {
          return;
        }
        int keyCode = nativeEvent.getKeyCode();

        if (keyCode == '?') {
          toggleKeyboardShortcuts();
        }
        if (keyCode == 'f' || keyCode == 'F') {
          toggleFullScreen(isFullScreen = !isFullScreen);
        }
      }
    };
    Event.addNativePreviewHandler(handler);

  }

  private void createNullableLayout() {
    search = new SimplePanel() {
      @Override
      public void setWidget(final IsWidget w) {
        ensureAttached(w);
        hasSearch = w != null;
        setSearchVisible(hasSearch);
        super.setWidget(w);
      }
    };
    ActivityManager searchActivityManager = new ActivityManager(searchActivityMapper, eventBus);
    search.ensureDebugId("search");
    searchActivityManager.setDisplay(search);

    // 页脚
    footer = new SimplePanel() {
      @Override
      public void setWidget(final IsWidget w) {
        ensureAttached(w);
        hasFooter = w != null;
        setFooterVisible(hasFooter);
        super.setWidget(w);
      }
    };
    ActivityManager footerActivityManager = new ActivityManager(footerActivityMapper, eventBus);
    footer.ensureDebugId("footer");
    footerActivityManager.setDisplay(footer);

    // 左侧竖排导航栏
    nav = new SimplePanel() {
      @Override
      public void setWidget(final IsWidget w) {
        ensureAttached(w);
        hasNav = w != null;
        setNavVisible(hasNav);
        super.setWidget(w);
      }
    };
    ActivityManager navActivityManager = new ActivityManager(navActivityMapper, eventBus);
    nav.ensureDebugId("nav");
    navActivityManager.setDisplay(nav);
  }

  private void ensureAttached(final IsWidget w) {
    if (this.isAttached()) {
      return;
    }
    if (w == null) {
      return;
    }
    RootLayoutPanel.get().clear();
    RootLayoutPanel.get().add(this);
  }

  private void setFooterVisible(final boolean visible) {
    if (!visible ^ footer.isAttached()) {
      // 同或, 当前状态与期望状态一致
      return;
    }
    if (visible && !isFullScreen && hasFooter) {
      rootLayout.insertSouth(footer, 0, center);
      rootLayout.forceLayout();
      rootLayout.setWidgetSize(footer, Footer.HEIGHT);
      rootLayout.animate(ANIMATE_DURATION);
    } else {
      rootLayout.remove(footer);
      rootLayout.animate(ANIMATE_DURATION);
    }
  }

  private void setNavVisible(final boolean visible) {
    if (!visible ^ nav.isAttached()) {
      // 同或, 当前状态与期望状态一致
      return;
    }
    if (visible && !isFullScreen && hasNav) {
      rootLayout.insertLineStart(nav, 0, center);
      rootLayout.forceLayout();
      rootLayout.setWidgetSize(nav, Nav.WIDTH);
      rootLayout.animate(ANIMATE_DURATION);
    } else {
      rootLayout.remove(nav);
      rootLayout.animate(ANIMATE_DURATION);
    }
  }

  private void setSearchVisible(final boolean visible) {
    if (!visible ^ search.isAttached()) {
      // 同或, 当前状态与期望状态一致
      return;
    }
    if (visible && !isFullScreen && hasSearch) {
      rootLayout.insertNorth(search, 0, center);
      rootLayout.forceLayout();
      rootLayout.setWidgetSize(search, Search.HEIGHT);
      rootLayout.animate(ANIMATE_DURATION);
    } else {
      rootLayout.remove(search);
      rootLayout.animate(ANIMATE_DURATION);
    }
  }

  private void toggleFullScreen(final boolean fullScreen) {
    if (fullScreen) {
      logger.info("全屏(F)");

      setSearchVisible(false);
      setFooterVisible(false);
      setNavVisible(false);
    } else {
      logger.info("取消全屏(F)");

      setSearchVisible(true);
      setFooterVisible(true);
      setNavVisible(true);
    }
  }

  private void toggleKeyboardShortcuts() {
    keyboardShortcutsProvider.get(new AsyncCallback<KeyboardShortcuts>() {

      @Override
      public void onFailure(final Throwable caught) {
        if (LogConfiguration.loggingIsEnabled()) {
          logger.log(Level.WARNING, "加载失败, 请重试", caught);
        }
      }

      @Override
      public void onSuccess(final KeyboardShortcuts result) {
        result.toggleVisible();
      }
    });

  }
}
