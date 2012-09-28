package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

import java.util.HashMap;
import java.util.Map;

public interface BaseViewBrowser {

  ProvidesKey<Book> keyProvider = new ProvidesKey<Book>() {

    @Override
    public Object getKey(final Book item) {
      return item == null ? null : item.getId();
    }
  };

  Map<String, ColumnEntity<?>> columns = new HashMap<String, ColumnEntity<?>>();

  boolean isChecked = false;

  public <T extends AbstractHasData<Book>> T getCellView();

  public Widget getView();

  public boolean isChecked();

  public void refresh();
}
