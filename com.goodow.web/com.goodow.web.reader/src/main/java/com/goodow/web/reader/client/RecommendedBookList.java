package com.goodow.web.reader.client;

import com.google.gwt.user.client.ui.Image;
import com.google.inject.Singleton;

import com.googlecode.mgwt.ui.client.widget.Carousel;

@Singleton
public class RecommendedBookList extends AbstractBookList {

  // @Override
  // public void refresh() {
  // bookService.getSelectedBooks().fire(this);
  // }

  @Override
  protected void start() {
    super.start();
    Carousel carsousel = new Carousel();
    carsousel.add(new Image("http://www.retechcorp.com/Img/service_1.jpg"));
    carsousel.add(new Image("http://www.retechcorp.com/Img/service_1.jpg"));
    carsousel.add(new Image("http://www.retechcorp.com/Img/service_1.jpg"));
    carsousel.add(new Image("http://www.retechcorp.com/Img/service_1.jpg"));
    container.insert(carsousel, 0);
    container.add(bookList);
  }
}
