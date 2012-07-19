package com.goodow.web.reader.client;

import com.goodow.web.core.client.WebView;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.reader.client.style.ReadResources;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

import java.util.List;

public class CategorizedBookList extends WebView<HTMLPanel> implements Receiver<List<Book>> {

  HTMLPanel bookList;

  @Inject
  AsyncBookService bookService;
  @Inject
  Provider<BookSummary> bookSummaryProvider;

  CategoryListView categoryListView;

  @Override
  public void onSuccess(final List<Book> result) {
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
    return BookSummary.INSTANCE.landscape().root1();
  }

  @Override
  public void start() {
    bookList = new HTMLPanel("");

    categoryListView = new CategoryListView();

    main.addStyleName(ReadResources.INSTANCE().categroyListCss().categorContainer());

    ScrollPanel scrollPanel = new ScrollPanel();
    scrollPanel.setWidget(bookList);
    scrollPanel.setScrollingEnabledX(false);
    scrollPanel.setScrollingEnabledY(true);

    main.add(categoryListView);
    main.add(scrollPanel);

    new Timer() {
      @Override
      public void run() {
        refresh();
      }
    }.schedule(1);
  }

  @Override
  protected HTMLPanel createRoot() {
    return new HTMLPanel("");
  }

}