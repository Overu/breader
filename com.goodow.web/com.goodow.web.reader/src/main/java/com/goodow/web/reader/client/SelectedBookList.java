package com.goodow.web.reader.client;

import com.google.inject.Singleton;

@Singleton
public class SelectedBookList extends BookList {

  public static final String NAME = "selected";

  @Override
  public void refresh() {
    bookService.getSelectedBooks().fire(this);
  }
}
