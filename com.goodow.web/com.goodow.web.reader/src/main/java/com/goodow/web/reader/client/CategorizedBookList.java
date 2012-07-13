package com.goodow.web.reader.client;

import com.goodow.web.reader.client.style.ReadResources;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

import java.util.ArrayList;
import java.util.List;

public class CategorizedBookList extends Composite {

  HTMLPanel bookList;

  HTMLPanel container;

  CategoryListView categoryListView;

  @Inject
  public CategorizedBookList(final Provider<BookSummary> bookSummaryProvider) {

    container = new HTMLPanel("");
    bookList = new HTMLPanel("");
    categoryListView = new CategoryListView();

    container.addStyleName(ReadResources.INSTANCE().categroyListCss().categorContainer());

    for (Book book : createBooks()) {
      BookSummary view = bookSummaryProvider.get();
      view.setLandscape(setBookSummaryLandscapeCss());
      view.setBook(book);
      bookList.add(view);
    }

    ScrollPanel scrollPanel = new ScrollPanel();
    scrollPanel.setWidget(bookList);
    scrollPanel.setScrollingEnabledX(false);
    scrollPanel.setScrollingEnabledY(true);

    container.add(categoryListView);
    container.add(scrollPanel);

    initWidget(container);
  }

  public String setBookSummaryLandscapeCss() {
    return BookSummary.INSTANCE.landscape().root1();
  }

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