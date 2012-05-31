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
import com.goodow.web.mvp.shared.tree.TreeNodePlace;
import com.goodow.web.mvp.shared.tree.TreeNodeProxy;
import com.goodow.web.mvp.shared.tree.event.RefreshEvent;
import com.goodow.web.mvp.shared.tree.event.RefreshRequestEvent;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.TakesValue;
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
public class TreeNodeDataProvider extends AsyncDataProvider<TreeNodeProxy> implements
    TakesValue<TreeNodeProxy>, ActivityAware {

  private TreeNodeProxy parent;
  private final Logger logger = Logger.getLogger(getClass().getName());

  private final Provider<TagsUi> tree;
  private final EventBus eventBus;
  private final PlaceController placeController;

  @Inject
  private TreeNodeDataProvider(final Provider<TagsUi> tree, final EventBus eventBus,
      final PlaceController placeController) {
    this.tree = tree;
    this.eventBus = eventBus;
    this.placeController = placeController;
  }

  @Override
  public TreeNodeProxy getValue() {
    return parent;
  }

  @Override
  public void onStart(final ActivityState state) {
    RefreshEvent.Handler<TreeNodeProxy> handler = new RefreshEvent.Handler<TreeNodeProxy>() {

      @Override
      public void onRefresh(final RefreshEvent<TreeNodeProxy> event) {
        TreeNodeProxy parentNode = event.getValue();
        if (parentNode != null && parentNode.equals(parent)) {
          refresh(parentNode);
          return;
        }
        if (parent == null) {
          Place where = placeController.getWhere();
          if (where instanceof TreeNodePlace) {
            TreeNodeProxy top = ((TreeNodePlace) where).getTopNode();
            if (top != null) {
              parent = top;
              onRangeChanged(null);
            }
          }
        }
      }
    };
    state.getEventBus().addHandler(RefreshEvent.TYPE, handler);

  }

  public boolean refresh(final TreeNodeProxy parent) {
    if (parent == null) {
      return false;
    }
    List<TreeNodeProxy> children = parent.getChildren();
    if (children == null || children.isEmpty()) {
      return false;
    }
    logger.finest("refresh");
    updateRowCount(children.size(), true);
    updateRowData(0, children);

    tree.get().select();
    return true;
  }

  @Override
  public void setValue(final TreeNodeProxy value) {
    this.parent = value;
  }

  @Override
  protected void onRangeChanged(final HasData<TreeNodeProxy> view) {
    boolean success = refresh(parent);
    if (!success && parent != null) {
      RefreshRequestEvent<String> refreshRequestEvent = new RefreshRequestEvent<String>();
      refreshRequestEvent.setValue(parent.getPath());
      eventBus.fireEvent(refreshRequestEvent);
    }
  }

}