package com.goodow.web.reader.client;

import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.Request;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Singleton;

import com.googlecode.mgwt.ui.client.widget.MSearchBox;

import java.util.List;

@Singleton
public class SearchBookList extends AbstractBookList {

  private MSearchBox searchBox;

  @Override
  public void refresh() {
  }

  @Override
  protected void start() {
    super.start();
    searchBox = new MSearchBox();
    searchBox.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        String value = event.getValue();
        if (value == null || value.equals("")) {
          return;
        }
        bookService.getBookByTitl(value).fire(new Receiver<List<Book>>() {
          @Override
          public void onSuccess(List<Book> result) {
            SearchBookList.super.onSuccess(result);
          }
        });
      }
    });
    container.insert(searchBox, 0);
  }
}
