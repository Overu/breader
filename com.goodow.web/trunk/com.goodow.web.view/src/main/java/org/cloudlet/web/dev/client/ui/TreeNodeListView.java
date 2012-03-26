package org.cloudlet.web.dev.client.ui;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.Provider;

import org.cloudlet.web.mvp.shared.ActivityAware;
import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.mvp.shared.tree.TreeNodeProxy;
import org.cloudlet.web.mvp.shared.tree.rpc.TreeNodeDataProvider;
import org.cloudlet.web.mvp.shared.tree.rpc.TreeNodeFactory;

public class TreeNodeListView extends Composite implements ActivityAware {
  interface Binder extends UiBinder<Widget, TreeNodeListView> {
  }

  interface TableResources extends DataGrid.Resources {
    @Override
    @Source(value = {DataGrid.Style.DEFAULT_CSS, "DataGridPatch.css"})
    DataGrid.Style dataGridStyle();
  }

  private static class NameCol extends TextColumn<TreeNodeProxy> implements HasName {

    private String name;

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getValue(final TreeNodeProxy object) {
      return object.getName();
    }

    @Override
    public void setName(final String name) {
      this.name = name;
    }

  }
  private static class PathCol extends Column<TreeNodeProxy, String> implements HasName {
    private String name;

    public PathCol() {
      super(new TextCell());
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getValue(final TreeNodeProxy object) {
      return object.getPath();
    }

    @Override
    public void setName(final String name) {
      this.name = name;
    }
  }

  private static final int NUM_ROWS = 10;

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  DockLayoutPanel dock;

  @UiField(provided = true)
  SimplePager pager = new SimplePager();

  @UiField(provided = true)
  DataGrid<TreeNodeProxy> grid;

  private final PlaceController placeController;

  private final TreeNodeFactory f;

  private final Provider<BasePlace> place;

  private ActivityState state;

  private final TreeNodeDataProvider dtatProvider;

  @Inject
  TreeNodeListView(final TreeNodeFactory f,
      final SingleSelectionModel<TreeNodeProxy> selectionModel,
      final PlaceController placeController, final Provider<BasePlace> place,
      final TreeNodeDataProvider dtatProvider) {
    this.placeController = placeController;
    this.place = place;
    this.dtatProvider = dtatProvider;
    this.f = f;

    grid = new DataGrid<TreeNodeProxy>(NUM_ROWS, GWT.<TableResources> create(TableResources.class));

    grid.setSelectionModel(selectionModel);
    grid.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

    PathCol pathCol = new PathCol();
    // 指定后台排序的字段名
    pathCol.setName("path");
    pathCol.setSortable(true);
    grid.addColumn(pathCol, "Path");
    grid.setColumnWidth(pathCol, "40ex");

    NameCol nameCol = new NameCol();
    nameCol.setName("name");
    nameCol.setSortable(true);
    grid.addColumn(nameCol, "Name");
    grid.setColumnWidth(nameCol, "25ex");

    grid.addColumnSortHandler(new ColumnSortEvent.AsyncHandler(grid));
    grid.setRowCount(NUM_ROWS, false);
    dtatProvider.addDataDisplay(grid);
    pager.setDisplay(grid);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        TreeNodeProxy proxy = selectionModel.getSelectedObject();
        BasePlace newPlace =
            place.get().setParameter(state.getName(), f.getHistoryToken(proxy.stableId()));
        placeController.goTo(newPlace);
      }
    });
    initWidget(binder.createAndBindUi(this));
  }

  @Override
  public void onStart(final ActivityState state) {
    this.state = state;
  }

  @UiHandler("create")
  void onCreate(final ClickEvent event) {
    placeController.goTo(place.get().setParameter(state.getName(),
        f.getHistoryToken(TreeNodeProxy.class)));
  }
}
