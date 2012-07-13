package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.ui.client.widget.Carousel;

import java.util.ArrayList;
import java.util.List;

public class RecommendedBookList extends AbstractBookList {

  @Inject
  public RecommendedBookList(final Provider<BookSummary> bookSummaryProvider) {
    super(bookSummaryProvider);
    bookList.removeFromParent();
    Carousel carsousel = new Carousel();
    carsousel.add(new Image("http://www.retechcorp.com/images/temp/wancai.jpg"));
    carsousel.add(new Image("http://www.retechcorp.com/images/temp/zx.jpg"));
    carsousel.add(new Image("http://www.retechcorp.com/images/temp/szcb.jpg"));
    carsousel.add(new Image("http://www.retechcorp.com/images/temp/el.jpg"));
    container.add(carsousel);
    container.add(bookList);
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
