package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.WavePanel;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.retech.reader.web.shared.proxy.CategoryProxy;
import com.retech.reader.web.shared.rpc.CategoryDataProvider;

import org.cloudlet.web.mvp.shared.BasePlace;

@Singleton
public class CategoryListEditor extends WavePanel implements Activity {

  private final CellList<CategoryProxy> cellList;
  private final NoSelectionModel<CategoryProxy> selectionModel;

  private final CategoryDataProvider categoryDataProvider;

  @Inject
  CategoryListEditor(final CategoryDataProvider categoryDataProvider,
      final PlaceController placeController, final Provider<BasePlace> places,
      final CategoryProxyCell cell, final CellList.Resources resources) {
    this.categoryDataProvider = categoryDataProvider;

    cellList = new CellList<CategoryProxy>(cell, resources);
    selectionModel = new NoSelectionModel<CategoryProxy>();
    cellList.setSelectionModel(selectionModel);

    setContent(cellList);
    title().setText("类别");

    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        placeController.goTo(places.get().setPath(BookListEditor.class.getName()).setParameter(
            selectionModel.getLastSelectedObject().stableId()));
      }
    });
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
    if (!categoryDataProvider.getDataDisplays().contains(cellList)) {
      categoryDataProvider.addDataDisplay(cellList);
    }
  }

  @Override
  protected void onUnload() {
    if (categoryDataProvider.getDataDisplays().contains(cellList)) {
      categoryDataProvider.removeDataDisplay(cellList);
    }
    super.onUnload();
  }

}
