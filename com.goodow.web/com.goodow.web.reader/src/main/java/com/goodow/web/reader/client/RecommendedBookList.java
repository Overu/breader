package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.ui.client.widget.touch.TouchPanel;

import java.util.ArrayList;
import java.util.List;

public class RecommendedBookList extends Composite {

  TouchPanel container;

  private final Provider<BookSummary> bookViewProvider;

  @Inject
  public RecommendedBookList(final Provider<BookSummary> bookViewProvider) {
    this.bookViewProvider = bookViewProvider;
    // title.setText("精品推荐");
    container = new TouchPanel();
    HTMLPanel bookList = new HTMLPanel("");
    for (Book book : createBooks()) {
      BookSummary view = bookViewProvider.get();
      view.setBook(book);
      bookList.add(view);
    }

    container.add(bookList);

    initWidget(container);

  }

  private List<Book> createBooks() {
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
