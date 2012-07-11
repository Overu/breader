package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.ui.client.widget.touch.TouchPanel;

import java.util.List;

public abstract class AbstractBookList extends Composite {

  TouchPanel container;

  @Inject
  public AbstractBookList(final Provider<BookSummary> bookSummaryProvider) {
    // title.setText("精品推荐");
    container = new TouchPanel();
    HTMLPanel bookList = new HTMLPanel("");
    for (Book book : createBooks()) {
      BookSummary view = bookSummaryProvider.get();
      view.setBook(book);
      bookList.add(view);
    }

    container.add(bookList);

    initWidget(container);

  }

  protected abstract List<Book> createBooks();
}
