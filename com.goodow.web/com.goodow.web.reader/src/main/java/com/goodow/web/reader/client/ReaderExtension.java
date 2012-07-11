package com.goodow.web.reader.client;

import com.goodow.web.core.client.UIRegistry;
import com.goodow.web.core.shared.HomePlace;
import com.goodow.web.core.shared.MyPlace;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.googlecode.mgwt.mvp.client.Animation;

@Singleton
public class ReaderExtension {

  @Inject
  UIRegistry registry;

  @Inject
  AsyncProvider<BookShelfView> bookshelf;

  @Inject
  AsyncProvider<BookStoreView> bookstore;

  @Inject
  Provider<RecommendedBookList> recommended;

  @Inject
  Provider<DiscountBookList> discounted;

  @Inject
  Provider<MostViewedBookList> mostViewed;

  @Inject
  Provider<CategorizedBookList> categorized;

  @HomePlace
  @Inject
  MyPlace homePlace;

  @Inject
  MyPlace bookshelfPlace;

  @Inject
  MyPlace bookstorePlace;

  @Inject
  MyPlace recommendedPlace;

  @Inject
  MyPlace discountedPlace;

  @Inject
  MyPlace mostViewedPlace;

  @Inject
  MyPlace categorizedPlace;

  public void registerExtensions() {
    homePlace.setAnimation(Animation.FADE);
    homePlace.addChild(bookshelfPlace);
    homePlace.addChild(bookstorePlace);

    bookshelfPlace.setPath("bookshelf");
    bookshelfPlace.setAnimation(Animation.FLIP);
    bookshelfPlace.setWidget(bookshelf);

    bookstorePlace.setPath("bookstore");
    bookstorePlace.setAnimation(Animation.FLIP);
    bookstorePlace.setWidget(bookstore);
    bookstorePlace.addChild(recommendedPlace);
    bookstorePlace.addChild(discountedPlace);
    bookstorePlace.addChild(mostViewedPlace);
    bookstorePlace.addChild(categorizedPlace);

    recommendedPlace.setPath("recommended");
    recommendedPlace.setAnimation(Animation.SLIDE);
    recommendedPlace.setWidget(recommended);

    discountedPlace.setPath("discounted");
    discountedPlace.setAnimation(Animation.SLIDE);
    discountedPlace.setWidget(discounted);

    mostViewedPlace.setPath("mostviewed");
    mostViewedPlace.setAnimation(Animation.SLIDE);
    mostViewedPlace.setWidget(mostViewed);

    categorizedPlace.setPath("categorized");
    categorizedPlace.setAnimation(Animation.SLIDE);
    categorizedPlace.setWidget(categorized);
  }
}