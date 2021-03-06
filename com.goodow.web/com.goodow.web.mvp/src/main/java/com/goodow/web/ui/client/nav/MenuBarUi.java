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
import com.goodow.web.mvp.shared.BasePlace;
import com.goodow.web.mvp.shared.Default;
import com.goodow.web.mvp.shared.tree.TreeNodePlace;
import com.goodow.web.mvp.shared.tree.TreeNodeProxy;
import com.goodow.web.mvp.shared.tree.event.RefreshEvent;
import com.goodow.web.mvp.shared.tree.event.RefreshRequestEvent;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class MenuBarUi extends Composite implements ActivityAware {

  private MenuBar bar;
  private final PlaceController placeController;
  private final Logger logger = Logger.getLogger(getClass().getName());
  private final Provider<TreeNodePlace> placeProvider;
  private final TreeNodeProxy root;

  @Inject
  MenuBarUi(final Provider<TreeNodePlace> placeProvider, final PlaceController placeController,
      @Default final TreeNodeProxy root) {
    this.root = root;
    logger.finest("init start");

    this.placeProvider = placeProvider;
    this.placeController = placeController;
    bar = new MenuBar();
    bar.setAutoOpen(true);
    bar.setWidth("100%");
    bar.setAnimationEnabled(true);

    initWidget(bar);
    if (LogConfiguration.loggingIsEnabled()) {
      logger.log(Level.FINEST, "init end");
    }
  }

  @Override
  public void onStart(final ActivityState state) {
    RefreshEvent.Handler<TreeNodeProxy> handler = new RefreshEvent.Handler<TreeNodeProxy>() {

      @Override
      public void onRefresh(final RefreshEvent<TreeNodeProxy> event) {
        TreeNodeProxy parent = event.getValue();
        if (BasePlace.PATH_SEPARATOR.equals(parent.getPath())) {
          setMenuData(bar, parent);
        }
      }
    };
    state.getEventBus().addHandler(RefreshEvent.TYPE, handler);
    if (root.getChildren() != null) {
      setMenuData(bar, root);
    } else {
      RefreshRequestEvent<String> refreshRequestEvent = new RefreshRequestEvent<String>();
      refreshRequestEvent.setValue(BasePlace.PATH_SEPARATOR);
      state.getEventBus().fireEvent(refreshRequestEvent);
    }

  }

  private void setMenuData(final MenuBar parentBar, final TreeNodeProxy parentNode) {
    List<TreeNodeProxy> childNodes = parentNode.getChildren();
    if (childNodes == null) {
      return;
    }
    parentBar.clearItems();
    for (final TreeNodeProxy childNode : childNodes) {
      Command command = new Command() {
        @Override
        public void execute() {
          TreeNodePlace place = placeProvider.get().setPath(childNode.getPath());
          placeController.goTo(place);
        }
      };
      parentBar.addItem(childNode.getName(), command);
      parentBar.addSeparator();
    }
    // for (final TreeNode childNode : childNodes) {
    // if (childNode.getChildren() == null || childNode.getChildren().isEmpty()) {
    // Command command = new Command() {
    // public void execute() {
    // TreeNodePlace place = placeProvider.get();
    // place.setMenuId(childNode.getId());
    // placeController.goTo(place);
    // }
    // };
    // parentBar.addItem(childNode.getName(), command);
    // } else {
    // MenuBar childBar = new MenuBar(true);
    // childBar.setAnimationEnabled(true);
    //
    // setMenuData(childBar, childNode);
    // parentBar.addItem(childNode.getName(), childBar);
    // parentBar.addSeparator();
    // }
    // }
  }
}
