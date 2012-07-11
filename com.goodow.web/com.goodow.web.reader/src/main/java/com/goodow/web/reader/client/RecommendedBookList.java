package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.widget.Carousel;

import java.util.ArrayList;
import java.util.List;

public class RecommendedBookList extends AbstractBookList {

  @Inject
  public RecommendedBookList(final Provider<BookSummary> bookSummaryProvider) {
    super(bookSummaryProvider);
    setTitle("精品推荐");
    bookList.removeFromParent();

    Carousel carsousel = new Carousel();
    carsousel.add(new Image("http://img.soufun.com/news/2012_07/11/news/1341969513496_000.jpg"));
    carsousel.add(new Image(
        "http://img03.taobaocdn.com/bao/uploaded/i3/T1405_XkRoXXcKWqU._081321.jpg_250x250.jpg"));
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

  @Override
  protected ImageResource getButtonImage() {
    return MGWTStyle.getTheme().getMGWTClientBundle().tabBarFavoritesImage();
  }

  @Override
  protected String getButtonText() {
    return "推荐";
  }
}
