package com.goodow.web.reader.client;

import com.goodow.web.core.shared.HomePlace;
import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.MGWTClientBundle;

@Singleton
public class ReaderPlugin {

  @Inject
  AsyncProvider<BookShelf> bookshelf;

  @Inject
  AsyncProvider<BookStore> bookstore;

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
  WebPlace homePlace;

  @Inject
  WebPlace bookshelfPlace;

  @Inject
  WebPlace bookstorePlace;

  @Inject
  WebPlace recommendedPlace;

  @Inject
  WebPlace discountedPlace;

  @Inject
  WebPlace mostViewedPlace;

  @Inject
  WebPlace categorizedPlace;

  public void start() {

    MGWTClientBundle bundle = MGWTStyle.getTheme().getMGWTClientBundle();

    homePlace.setAnimation(Animation.FLIP);
    homePlace.setWelcomePlace(bookshelfPlace);
    homePlace.addChild(bookstorePlace);

    bookshelfPlace.setPath("bookshelf");
    bookshelfPlace.setAnimation(Animation.SLIDE);
    bookshelfPlace.setWidget(bookshelf);

    bookstorePlace.setPath("bookstore");
    bookstorePlace.setAnimation(Animation.SLIDE);
    bookstorePlace.setWidget(bookstore);
    bookstorePlace.setWelcomePlace(recommendedPlace);
    bookstorePlace.addChild(discountedPlace);
    bookstorePlace.addChild(mostViewedPlace);
    bookstorePlace.addChild(categorizedPlace);

    recommendedPlace.setPath("recommended");
    recommendedPlace.setAnimation(Animation.SLIDE);
    recommendedPlace.setWidget(recommended);
    recommendedPlace.setTitle("精品推荐");
    recommendedPlace.setButtonText("推荐");
    recommendedPlace.setButtonImage(bundle.tabBarFavoritesImage());

    discountedPlace.setPath("discounted");
    discountedPlace.setAnimation(Animation.SLIDE);
    discountedPlace.setWidget(discounted);
    discountedPlace.setTitle("特价促销");
    discountedPlace.setButtonText("特价");
    discountedPlace.setButtonImage(bundle.tabBarHistoryImage());

    mostViewedPlace.setPath("mostviewed");
    mostViewedPlace.setAnimation(Animation.SLIDE);
    mostViewedPlace.setWidget(mostViewed);
    mostViewedPlace.setTitle("热门图书");
    mostViewedPlace.setButtonText("热门");
    mostViewedPlace.setButtonImage(bundle.tabBarMostViewedImage());

    categorizedPlace.setPath("categorized");
    categorizedPlace.setAnimation(Animation.SLIDE);
    categorizedPlace.setWidget(categorized);
    categorizedPlace.setTitle("分类浏览");
    categorizedPlace.setButtonText("分类");
    categorizedPlace.setButtonImage(bundle.tabBarBookMarkImage());
  }
}