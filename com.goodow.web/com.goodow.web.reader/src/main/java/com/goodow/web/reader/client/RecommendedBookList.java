package com.goodow.web.reader.client;

import com.google.gwt.user.client.ui.Image;
import com.google.inject.Singleton;

import com.googlecode.mgwt.ui.client.widget.Carousel;

@Singleton
public class RecommendedBookList extends AbstractBookList {

  @Override
  public void refresh() {
    bookService.getSelectedBooks().fire(this);
  }

  @Override
  protected void start() {
    super.start();
    bookList.removeFromParent();
    Carousel carsousel = new Carousel();
    carsousel.add(new Image("http://www.retechcorp.com/images/temp/wancai.jpg"));
    carsousel.add(new Image("http://www.retechcorp.com/images/temp/zx.jpg"));
    carsousel.add(new Image("http://www.retechcorp.com/images/temp/szcb.jpg"));
    carsousel.add(new Image("http://www.retechcorp.com/images/temp/el.jpg"));
    container.add(carsousel);
    container.add(bookList);
  }

}
