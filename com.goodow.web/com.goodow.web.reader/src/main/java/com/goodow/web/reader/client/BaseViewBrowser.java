package com.goodow.web.reader.client;

import com.goodow.web.core.shared.Receiver;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseViewBrowser extends Composite implements Receiver<List<Book>> {

  ProvidesKey<Book> keyProvider = new ProvidesKey<Book>() {

    @Override
    public Object getKey(final Book item) {
      return item == null ? null : item.getId();
    }
  };

  public static final Map<String, ColumnEntity<?>> columns = new HashMap<String, ColumnEntity<?>>();

  protected ListDataProvider<Book> dataProvider;
  protected MultiSelectionModel<Book> selectionModel;

  @Inject
  protected AsyncBookService bookService;

  boolean isChecked = false;

  public BaseViewBrowser() {
    dataProvider = new ListDataProvider<Book>();
    selectionModel = new MultiSelectionModel<Book>(keyProvider);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        if (isChecked()) {
          BooksViewBrowser.enable();
          return;
        }
        BooksViewBrowser.diable();
      }

    });

    initWidget(init());
  }

  public abstract <T extends AbstractHasData<Book>> T getCellView();

  public abstract Widget getView();

  public boolean isChecked() {
    Set<Book> selectedSet = selectionModel.getSelectedSet();
    if (selectedSet.size() != 0) {
      return true;
    }
    return false;
  }

  @Override
  public void onSuccess(final List<Book> result) {
    dataProvider.setList(result);
    setListHandler();
    if (!dataProvider.getDataDisplays().contains(getCellView())) {
      dataProvider.addDataDisplay(getCellView());
    }
  }

  public abstract void refresh();

  public abstract void setListHandler();

  protected abstract Widget init();

  @Override
  protected void onLoad() {
    super.onLoad();
    refresh();
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    if (dataProvider.getDataDisplays().contains(getCellView())) {
      dataProvider.removeDataDisplay(getCellView());
    }
  }
}
