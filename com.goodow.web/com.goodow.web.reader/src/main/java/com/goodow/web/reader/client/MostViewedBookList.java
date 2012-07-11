package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.ui.client.MGWTStyle;

import java.util.ArrayList;
import java.util.List;

public class MostViewedBookList extends AbstractBookList {

  @Inject
  public MostViewedBookList(final Provider<BookSummary> bookSummaryProvider) {
    super(bookSummaryProvider);
    setTitle("热门图书");
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

  @Override
  protected ImageResource getButtonImage() {
    return MGWTStyle.getTheme().getMGWTClientBundle().tabBarMostViewedImage();
  }

  @Override
  protected String getButtonText() {
    return "热门";
  }
}
