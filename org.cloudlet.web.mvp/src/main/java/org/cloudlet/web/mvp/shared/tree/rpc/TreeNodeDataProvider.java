package org.cloudlet.web.mvp.shared.tree.rpc;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

import org.cloudlet.web.mvp.shared.tree.TreeNodeProxy;
import org.cloudlet.web.service.shared.QueryUtil;

import java.util.List;

public class TreeNodeDataProvider extends AsyncDataProvider<TreeNodeProxy> {

  private final TreeNodeFactory f;

  @Inject
  TreeNodeDataProvider(final TreeNodeFactory f) {
    this.f = f;
  }

  @Override
  protected void onRangeChanged(final HasData<TreeNodeProxy> display) {
    final Range range = display.getVisibleRange();

    f.treeNodeContext().find(range.getStart(), range.getLength(), QueryUtil.getSort(display)).to(
        new Receiver<List<TreeNodeProxy>>() {

          @Override
          public void onSuccess(final List<TreeNodeProxy> response) {
            updateRowData(range.getStart(), response);
          }
        }).fire();
  }
}
