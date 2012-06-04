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
import com.goodow.web.mvp.shared.tree.TreeNodeProxy;
import com.goodow.web.mvp.shared.tree.event.RefreshEvent;
import com.goodow.web.mvp.shared.tree.event.RefreshRequestEvent;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;


import java.util.List;
import java.util.logging.Logger;

/**
 * The {@link ListDataProvider} used for menu tree.
 */
public class TopTreeNodeDataProvider extends AsyncDataProvider<TreeNodeProxy> implements
    ActivityAware {

  private final Logger logger = Logger.getLogger(getClass().getName());
  private final EventBus eventBus;
  private final Provider<TopNavUi> ui;

  @Inject
  private TopTreeNodeDataProvider(final EventBus eventBus, final Provider<TopNavUi> ui) {
    this.eventBus = eventBus;
    this.ui = ui;
  }

  @Override
  public void onStart(final ActivityState state) {
    RefreshEvent.Handler<TreeNodeProxy> handler = new RefreshEvent.Handler<TreeNodeProxy>() {

      @Override
      public void onRefresh(final RefreshEvent<TreeNodeProxy> event) {
        TreeNodeProxy parent = event.getValue();
        if (BasePlace.PATH_SEPARATOR.equals(parent.getPath())) {
          refresh(parent);
        }
      }
    };
    state.getEventBus().addHandler(RefreshEvent.TYPE, handler);
  }

  @Override
  protected void onRangeChanged(final HasData<TreeNodeProxy> view) {
    RefreshRequestEvent<String> refreshRequestEvent = new RefreshRequestEvent<String>();
    refreshRequestEvent.setValue(BasePlace.PATH_SEPARATOR);
    eventBus.fireEvent(refreshRequestEvent);
  }

  private boolean refresh(final TreeNodeProxy root) {
    if (root == null) {
      return false;
    }
    List<TreeNodeProxy> children = root.getChildren();
    if (children == null) {
      return false;
    }
    logger.finest("refresh");
    updateRowCount(children.size(), true);
    updateRowData(0, children);
    ui.get().select();
    return true;
  }

}