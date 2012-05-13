package com.retech.reader.web.client.mobile.ui;

import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.wavepanel.WavePanelResources;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
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

    getWaveTitle().setText("类别");
    FlowPanel toDo = new FlowPanel();
    toDo.addStyleName(WavePanelResources.css().waveWarning());
    toDo.add(new HTML("<b>已完成：<b>"));
    toDo.add(new Label("5.1 数据库各个分类内容"));
    toDo.add(new HTML("<br>"));
    toDo.add(new HTML("<b>待实现：<b>"));
    toDo.add(new Label("5.2 我的藏书分类（易）"));
    toDo.add(new Label("5.3 特色分类（中）"));
    toDo.add(new Label("5.4 推荐分类（中）"));
    toDo.add(new Label("5.5 搜索分类（易）"));
    add(toDo);

    setWaveContent(cellList);
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
