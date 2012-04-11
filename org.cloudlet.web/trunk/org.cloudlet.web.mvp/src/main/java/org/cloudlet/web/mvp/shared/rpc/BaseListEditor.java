package org.cloudlet.web.mvp.shared.rpc;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.NoSelectionModel;

import org.cloudlet.web.service.shared.rpc.BaseEntityProxy;

public abstract class BaseListEditor<T extends BaseEntityProxy> extends Composite implements
    Activity {
  /**
   * Resources used by the mobile CellList.
   */
  interface CellListResources extends CellList.Resources {
    @Override
    @Source({CellList.Style.DEFAULT_CSS, "MobileCellList.css"})
    CellListStyle cellListStyle();
  }

  /**
   * Styles used by the mobile CellList.
   */
  interface CellListStyle extends CellList.Style {
  }

  protected CellList<T> cellList;

  protected NoSelectionModel<T> selectionModel;

  protected BaseListEditor() {
    // Create the CellList.
    CellListResources cellListRes = GWT.create(CellListResources.class);
    cellList = new CellList<T>(provideCell(), cellListRes);
    cellList.setPageSize(5);
    cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
    cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

    selectionModel = new NoSelectionModel<T>();
    cellList.setSelectionModel(selectionModel);
  }

  @Override
  public String mayStop() {
    return null;
  }

  @Override
  public void onCancel() {
  }

  @Override
  public void onStop() {
  }

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    if (!provideDataProvider().getDataDisplays().contains(cellList)) {
      provideDataProvider().addDataDisplay(cellList);
    }
  }

  protected void initEditor() {
    initWidget((Widget) provideUiBinder().createAndBindUi(this));
  }

  @Override
  protected void onUnload() {
    if (provideDataProvider().getDataDisplays().contains(cellList)) {
      provideDataProvider().removeDataDisplay(cellList);
    }
    super.onUnload();
  }

  protected abstract Cell<T> provideCell();

  protected abstract AbstractDataProvider<T> provideDataProvider();

  protected abstract UiBinder provideUiBinder();

}
