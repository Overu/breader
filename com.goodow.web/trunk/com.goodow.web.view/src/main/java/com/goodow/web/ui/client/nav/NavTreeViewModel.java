package com.goodow.web.ui.client.nav;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import org.cloudlet.web.mvp.shared.ActivityAware;
import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.tree.TreeNodeProxy;

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
  NavTreeViewModel(SingleSelectionModel<TreeNodeProxy> selectionModel, TreeNodeCell menuNodCell,
      Provider<TreeNodeDataProvider> dataProvider, PlaceController placeController) {
    this.placeController = placeController;
    logger.finest("init start");
    this.selectionModel = selectionModel;
    this.menuNodCell = menuNodCell;
    this.dataProvider = dataProvider;
    logger.finest("init end");
  }

  @Override
  public <T> NodeInfo<?> getNodeInfo(T value) {

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
  public boolean isLeaf(Object value) {
    TreeNodeProxy menuNode = (TreeNodeProxy) value;
    if (value == null) {
      return false;
    } else if (menuNode.getChildren() == null) {
      return true;
    }
    return false;
  }

  @Override
  public void onStart(ActivityState state) {
    this.state = state;
  }

}
