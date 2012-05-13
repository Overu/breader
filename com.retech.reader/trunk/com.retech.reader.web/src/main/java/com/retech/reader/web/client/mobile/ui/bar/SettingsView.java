package com.retech.reader.web.client.mobile.ui.bar;

import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.wavepanel.WavePanelResources;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.logging.Logger;

@Singleton
public class SettingsView extends WavePanel implements Activity {

  interface SettingsUiBinder extends UiBinder<Widget, SettingsView> {
  }

  private static final String CLEAR_DATA = "清除所有数据";

  private static final String CHECK_UPDATE = "检查更新";

  private static final Logger logger = Logger.getLogger(SettingsView.class.getName());

  @UiField(provided = true)
  CellTable<String> cellTable;
  private ListDataProvider<String> dataProvider = new ListDataProvider<String>();
  private static SettingsUiBinder uiBinder = GWT.create(SettingsUiBinder.class);

  @Inject
  SettingsView() {
    List<String> list = dataProvider.getList();
    list.add(CHECK_UPDATE);
    list.add(CLEAR_DATA);
    cellTable = new CellTable<String>();
    cellTable.setWidth("100%", true);
    // Add a text column to show the name.
    Column<String, String> column = new Column<String, String>(new ClickableTextCell()) {

      @Override
      public String getValue(final String object) {
        return object;
      }
    };
    column.setFieldUpdater(new FieldUpdater<String, String>() {

      @Override
      public void update(final int index, final String object, final String value) {
        if (value == CLEAR_DATA) {
          Storage.getLocalStorageIfSupported().clear();
          logger.info("数据已清除");
        } else {
          logger.info("开发中");
        }
      }
    });
    cellTable.addColumn(column, "");
    dataProvider.addDataDisplay(cellTable);

    getWaveTitle().setText("设置");

    FlowPanel toDo = new FlowPanel();
    toDo.addStyleName(WavePanelResources.css().waveWarning());
    toDo.add(new HTML("<b>已完成：<b>"));
    toDo.add(new Label("4.1 网站的离线使用"));
    toDo.add(new Label("4.2 少量数据的离线存储<5M"));
    toDo.add(new Label("4.3 清除所有已存储的数据"));
    toDo.add(new Label("4.4 3D特效的使用方式"));
    toDo.add(new Label("4.5 用户界面整体样式的部分设计"));
    toDo.add(new HTML("<br>"));
    toDo.add(new HTML("<b>待实现：<b>"));
    toDo.add(new Label("4.6 界面调整（易）"));
    toDo.add(new Label("4.7 大量数据的离线存储<50M（中）"));
    toDo.add(new Label("4.8 无限量数据的离线存储（难）"));
    toDo.add(new Label("4.9 检查更新（易）"));
    toDo.add(new Label("4.10 触摸屏手势的使用方式（中）"));
    toDo.add(new Label("4.11 字号设置（易）"));
    toDo.add(new Label("4.12 屏幕亮度设置（难）"));
    add(toDo);

    setWaveContent(uiBinder.createAndBindUi(this));
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
  }

}
