package org.cloudlet.web.ui.client.nav;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;

import org.cloudlet.web.mvp.shared.ActivityAware;
import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.mvp.shared.tree.TreeNodeProxy;
import org.cloudlet.web.mvp.shared.tree.event.RefreshEvent;
import org.cloudlet.web.mvp.shared.tree.event.RefreshRequestEvent;

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
  private TopTreeNodeDataProvider(EventBus eventBus, Provider<TopNavUi> ui) {
    this.eventBus = eventBus;
    this.ui = ui;
  }

  @Override
  public void onStart(ActivityState state) {
    RefreshEvent.Handler<TreeNodeProxy> handler = new RefreshEvent.Handler<TreeNodeProxy>() {

      @Override
      public void onRefresh(RefreshEvent<TreeNodeProxy> event) {
        TreeNodeProxy parent = event.getValue();
        if (BasePlace.PATH_SEPARATOR.equals(parent.getPath())) {
          refresh(parent);
        }
      }
    };
    state.getEventBus().addHandler(RefreshEvent.TYPE, handler);
  }

  @Override
  protected void onRangeChanged(HasData<TreeNodeProxy> view) {
    RefreshRequestEvent<String> refreshRequestEvent = new RefreshRequestEvent<String>();
    refreshRequestEvent.setValue(BasePlace.PATH_SEPARATOR);
    eventBus.fireEvent(refreshRequestEvent);
  }

  private boolean refresh(TreeNodeProxy root) {
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