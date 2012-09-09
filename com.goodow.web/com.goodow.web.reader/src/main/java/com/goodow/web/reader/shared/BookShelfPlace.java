package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebPlace;
import com.goodow.web.reader.client.BookShelf;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class BookShelfPlace extends WebPlace {

  @Inject
  public BookShelfPlace(final Provider<BookShelf> bookshelf) {
    setPath("bookshelf");
    setWidget(bookshelf);
  }

}
