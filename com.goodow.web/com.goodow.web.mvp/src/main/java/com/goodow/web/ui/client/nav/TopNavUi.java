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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;


import java.util.logging.Logger;

@Singleton
public class TopNavUi extends Composite implements ActivityAware {
  private final Logger logger = Logger.getLogger(getClass().getName());
  private final CellList<TreeNodeProxy> list;
  private final Provider<TreeNodePlace> placeProvider;
  private final PlaceController placeController;
  private boolean scheduled = false;
  private boolean fireEvent = true;
  private final SingleSelectionModel<TreeNodeProxy> selectionModel =
      new SingleSelectionModel<TreeNodeProxy>();
  private final TopTreeNodeDataProvider dataProvider;
  private ScheduledCommand cmd = new ScheduledCommand() {
    @Override
    public void execute() {
      scheduled = false;
      Place where = placeController.getWhere();
      if (where instanceof TreeNodePlace) {
        TreeNodePlace treeNodePlace = ((TreeNodePlace) where);
        TreeNodeProxy topNode = treeNodePlace.getTopNode();
        if (topNode == null) {
          return;
        }
        if (!selectionModel.isSelected(topNode)) {
          fireEvent = false;
          selectionModel.setSelected(topNode, true);
        }
      }
    }
  };

  @Inject
  TopNavUi(final TreeNodeCell cell, final TopTreeNodeDataProvider dataProvider,
      final Provider<TreeNodePlace> placeProvider, final PlaceController placeController) {
    logger.finest("init start");
    this.dataProvider = dataProvider;
    this.placeProvider = placeProvider;
    this.placeController = placeController;
    list = new CellList<TreeNodeProxy>(cell);
    list.setSelectionModel(selectionModel);

    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        TreeNodeProxy node = selectionModel.getSelectedObject();

        if (fireEvent) {
          TreeNodePlace place = placeProvider.get().setPath(node.getPath());
          placeController.goTo(place);
        } else {
          fireEvent = true;
        }
      }
    });

    initWidget(list);
    dataProvider.addDataDisplay(list);
    logger.finest("init end");
  }

  @Override
  public void onStart(final ActivityState state) {
    dataProvider.onStart(state);
    select();
  }

  public void select() {
    if (!scheduled) {
      scheduled = true;
      Scheduler.get().scheduleFinally(cmd);
    }
  }

}
