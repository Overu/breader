package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.ArrayList;
import java.util.List;

public class FavoriteBooks extends AbstractBookList {

  @Inject
  public FavoriteBooks(final Provider<BookSummary> bookSummaryProvider) {
    super(bookSummaryProvider);
  }

  @Override
  protected List<Book> createBooks() {
    List<Book> result = new ArrayList<Book>();
    for (int i = 0; i < 40; i++) {
      Book book = new Book();
      book.setTitle("小城三月" + (i + 1));
      book.setDescription("小城三月的故事");
      book.setAuthor("作者" + (i + 1));
      result.add(book);
    }
    return result;
  }

}
