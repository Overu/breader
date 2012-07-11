package com.goodow.web.reader.client;

import com.goodow.web.core.shared.HomePlace;
import com.goodow.web.core.shared.MyPlace;

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

  public void start() {

    MGWTClientBundle bundle = MGWTStyle.getTheme().getMGWTClientBundle();

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

    homePlace.setAnimation(Animation.FADE);
    homePlace.addChild(bookshelfPlace);
    homePlace.addChild(bookstorePlace);
  }
}