package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.ArrayList;
import java.util.List;

public class RecommendedBookList extends BookListView {

  FlowPanel container;

  private final Provider<BookSummary> bookViewProvider;

  @Inject
  public RecommendedBookList(final Provider<BookSummary> bookViewProvider) {
    this.bookViewProvider = bookViewProvider;
    title.setText("精品推荐");
    container = new FlowPanel();
    for (Book book : createBooks()) {
      BookSummary view = bookViewProvider.get();
      view.setBook(book);
      container.add(view);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Composite#onAttach()
   */
  @Override
  protected void onAttach() {
    super.onAttach();
    int columnWidth = main.getOffsetWidth() / 2;
    container.getElement().getStyle().setProperty("webkitColumnWidth", columnWidth + "px");
    container.removeFromParent();
    scrollPanel.setWidget(container);
  }

  private List<Book> createBooks() {
    List<Book> result = new ArrayList<Book>();
    for (int i = 0; i < 20; i++) {
      Book book = new Book();
      book.setTitle("小城三月" + (i + 1));
      book.setDescription("小城三月的故事");
      book.setAuthor("作者" + (i + 1));
      result.add(book);
    }
    return result;
  }
}
