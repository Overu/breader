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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.CellTree.Resources;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import org.cloudlet.web.mvp.shared.ActivityAware;
import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.tree.TreeNodePlace;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The tree located on the left of the app.
 */
@Singleton
public class TagsUi extends Composite implements ActivityAware {
  protected Resources res;
  private FlowPanel layout = new FlowPanel();

  /**
   * The last selected menu node.
   */
  private org.cloudlet.web.mvp.shared.tree.TreeNodeProxy lastTop;

  /**
   * The main widget.
   */
  protected CellTree tree;

  private final Resources resources;

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final SingleSelectionModel<org.cloudlet.web.mvp.shared.tree.TreeNodeProxy> selectionModel;

  private final NavTreeViewModel treeViewModel;

  private ScheduledCommand cmd = new ScheduledCommand() {
    @Override
    public void execute() {
      scheduled = false;
      Place where = placeController.getWhere();
      if (where instanceof TreeNodePlace) {
        TreeNodePlace place = (TreeNodePlace) where;

        if (!selectionModel.isSelected(place.getTreeNode())) {
          openTreeNodeWhenSelected(tree.getRootTreeNode(),
              org.cloudlet.web.mvp.shared.BasePlace.PATH_SEPARATOR + place.getPath()
                  + org.cloudlet.web.mvp.shared.BasePlace.PATH_SEPARATOR);
        }
      }
    }
  };

  private boolean scheduled = false;

  private final PlaceController placeController;

  @Inject
  private TagsUi(final NavTreeViewModel treeViewModel,
      final SingleSelectionModel<org.cloudlet.web.mvp.shared.tree.TreeNodeProxy> selectionModel,
      final Resources resources, final PlaceController placeController,
      final Provider<TreeNodePlace> placeProvider) {
    this.placeController = placeController;
    logger.finest("init start");
    this.treeViewModel = treeViewModel;
    this.selectionModel = selectionModel;

    this.resources = resources;
    // Listen for selection. We need to add this handler before the CellBrowser
    // adds its own handler.
    this.selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        org.cloudlet.web.mvp.shared.tree.TreeNodeProxy lastSelected =
            selectionModel.getSelectedObject();

        TreeNodePlace place = placeProvider.get().setPath(lastSelected.getPath());

        if (!place.equals(placeController.getWhere())) {
          placeController.goTo(place);
        }
      }
    });

    initWidget(layout);

    if (LogConfiguration.loggingIsEnabled()) {
      logger.log(Level.FINEST, "init end");
    }

  }

  @Override
  public void onStart(final ActivityState state) {
    treeViewModel.onStart(state);

    Place where = placeController.getWhere();
    if (where instanceof TreeNodePlace) {
      org.cloudlet.web.mvp.shared.tree.TreeNodeProxy top = ((TreeNodePlace) where).getTopNode();
      if (lastTop == null || lastTop != top) {
        createTree(top);
        lastTop = top;
      }
    }
  }

  public void select() {
    if (!scheduled) {
      scheduled = true;
      Scheduler.get().scheduleFinally(cmd);
    }
  }

  /**
   * Create the {@link CellTree}.
   */
  private CellTree createTree(final org.cloudlet.web.mvp.shared.tree.TreeNodeProxy root) {
    tree = new CellTree(treeViewModel, root, resources);
    tree.setAnimationEnabled(true);
    layout.clear();
    layout.add(tree);
    return tree;
  }

  private void openTreeNodeWhenSelected(final TreeNode parent, final String path) {
    logger.finest("openTreeNodeWhenSelected");
    for (int i = 0, len = parent.getChildCount(); i < len; i++) {
      org.cloudlet.web.mvp.shared.tree.TreeNodeProxy childNode =
          (org.cloudlet.web.mvp.shared.tree.TreeNodeProxy) parent.getChildValue(i);
      String childId = childNode.getPath();
      if (path.startsWith(childId)) {
        TreeNode childOpen = parent.setChildOpen(i, true, true);
        if (childOpen != null && !path.equals(childId)) {
          openTreeNodeWhenSelected(childOpen, path);
        } else if (path.equals(childId)) {
          if (!selectionModel.isSelected(childNode)) {
            selectionModel.setSelected(childNode, true);
          }
          continue;
        }
      } else {
        parent.setChildOpen(i, false, false);
      }
    }
  }

}
