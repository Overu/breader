package com.goodow.web.reader.client;

import com.goodow.web.core.client.ScrollView;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.ui.client.widget.touch.TouchPanel;

import java.util.List;

public abstract class AbstractBookList extends ScrollView implements Receiver<List<Book>> {

  TouchPanel container;

  HTMLPanel bookList;

  @Inject
  AsyncBookService bookService;
  @Inject
  Provider<BookSummary> bookSummaryProvider;

  @Override
  public void onSuccess(final List<Book> result) {
    bookList.clear();
    for (Book book : result) {
      BookSummary view = bookSummaryProvider.get();
      view.setLandscape(setBookSummaryLandscapeCss());
      view.setBook(book);
      bookList.add(view);
    }

  }

  @Override
  public void refresh() {
    bookService.getMyBooks().fire(this);
  }

  public String setBookSummaryLandscapeCss() {
    return BookSummary.INSTANCE.landscape().root();
  }

  @Override
  protected void start() {
    container = new TouchPanel();
    bookList = new HTMLPanel("");

    container.add(bookList);
    main.add(container);
    main.addStyleName(BooksBrowser.bundle.booksAppCss().main());

    new Timer() {
      @Override
      public void run() {
        refresh();
      }
    }.schedule(1);
  }
}
