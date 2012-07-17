package com.goodow.web.reader.client;

import com.google.inject.Singleton;

@Singleton
public class SelectedBookList extends BookList {
  @Override
  public void refresh() {
    bookService.getMyBooks().fire(this);
  }
}
