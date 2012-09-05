package com.goodow.web.reader.client;

import com.google.inject.Singleton;

@Singleton
public class MyBookList extends BookList {

  public static final String NAME = "mybooks";

  @Override
  public void refresh() {
    bookService.getMyBooks().fire(this);
  }
}
