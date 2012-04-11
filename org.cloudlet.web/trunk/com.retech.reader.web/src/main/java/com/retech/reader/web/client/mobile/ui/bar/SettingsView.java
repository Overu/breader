package com.retech.reader.web.client.mobile.ui.bar;

import com.goodow.web.view.wave.client.WavePanel;

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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.logging.Logger;

@Singleton
public class SettingsView extends Composite implements Activity {

  interface SettingsUiBinder extends UiBinder<Widget, SettingsView> {
  }

  private static final Logger logger = Logger.getLogger(SettingsView.class.getName());

  @UiField(provided = true)
  CellTable<String> cellTable;
  private ListDataProvider<String> dataProvider = new ListDataProvider<String>();
  private static SettingsUiBinder uiBinder = GWT.create(SettingsUiBinder.class);

  @Inject
  SettingsView(final WavePanel wavePanel) {
    List<String> list = dataProvider.getList();
    list.add("清除数据");
    list.add("清除应用程序");
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
        switch (index) {
          case 0:
            Storage.getLocalStorageIfSupported().clear();
            logger.info("操作成功");
            break;

          default:
            break;
        }
      }
    });
    cellTable.addColumn(column, "");
    dataProvider.addDataDisplay(cellTable);
    wavePanel.setContent(uiBinder.createAndBindUi(this));
    initWidget(wavePanel);
    wavePanel.title().setText("相关操作");
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
