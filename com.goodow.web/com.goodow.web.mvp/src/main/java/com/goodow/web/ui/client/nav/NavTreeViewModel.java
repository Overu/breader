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
import com.goodow.web.mvp.shared.tree.TreeNodeProxy;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;


import java.util.logging.Logger;

/**
 * The {@link TreeViewModel} used by the main menu.
 */
@Singleton
public class NavTreeViewModel implements TreeViewModel, ActivityAware {

  private final Logger logger = Logger.getLogger(getClass().getName());

  /**
   * The cell used to render examples.
   */

  private TreeNodeCell menuNodCell;

  private final Provider<TreeNodeDataProvider> dataProvider;

  private final SingleSelectionModel<TreeNodeProxy> selectionModel;

  private ActivityState state;

  private final PlaceController placeController;

  @Inject
  NavTreeViewModel(final SingleSelectionModel<TreeNodeProxy> selectionModel,
      final TreeNodeCell menuNodCell, final Provider<TreeNodeDataProvider> dataProvider,
      final PlaceController placeController) {
    this.placeController = placeController;
    logger.finest("init start");
    this.selectionModel = selectionModel;
    this.menuNodCell = menuNodCell;
    this.dataProvider = dataProvider;
    logger.finest("init end");
  }

  @Override
  public <T> NodeInfo<?> getNodeInfo(final T value) {

    AbstractDataProvider<TreeNodeProxy> dataProvider = this.dataProvider.get();
    if (dataProvider instanceof TakesValue) {
      ((TakesValue<T>) dataProvider).setValue(value);
    }
    if (dataProvider instanceof ActivityAware) {
      ((ActivityAware) dataProvider).onStart(state);
    }
    return new DefaultNodeInfo<TreeNodeProxy>(dataProvider, menuNodCell, selectionModel, null);
  }

  @Override
  public boolean isLeaf(final Object value) {
    TreeNodeProxy menuNode = (TreeNodeProxy) value;
    if (value == null) {
      return false;
    } else if (menuNode.getChildren() == null) {
      return true;
    }
    return false;
  }

  @Override
  public void onStart(final ActivityState state) {
    this.state = state;
  }

}
