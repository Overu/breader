package com.goodow.web.reader.client;

import com.goodow.web.core.shared.HomePlace;
import com.goodow.web.core.shared.WebPlace;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.MGWTClientBundle;

@Singleton
public class ReaderPlugin {

  @Inject
  Provider<BookShelf> bookshelf;

  @Inject
  Provider<BookStore> bookstore;

  @Inject
  Provider<RecommendedBookList> recommended;

  @Inject
  Provider<FavoriteBooks> favorites;

  @Inject
  Provider<DiscountBookList> discounted;

  @Inject
  Provider<MostViewedBookList> mostViewed;

  @Inject
  Provider<CategorizedBookList> categorized;

  @Inject
  Provider<AppLayout> appLayout;

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
  WebPlace favoritesPlace;

  @Inject
  WebPlace discountedPlace;

  @Inject
  WebPlace mostViewedPlace;

  @Inject
  WebPlace categorizedPlace;

  @Inject
  WebPlace appPlace;

  @Inject
  WebPlace booksPlace;

  @Inject
  WebPlace magazinesPlace;

  @Inject
  WebPlace newspapersPlace;

  public void start() {

    MGWTClientBundle bundle = MGWTStyle.getTheme().getMGWTClientBundle();

    homePlace.setAnimation(Animation.FLIP);
    homePlace.setWelcomePlace(bookshelfPlace);
    homePlace.addChild(bookstorePlace);
    homePlace.addChild(appPlace);

    bookshelfPlace.setPath("bookshelf");
    bookshelfPlace.setAnimation(Animation.SLIDE);
    bookshelfPlace.setWidget(bookshelf);

    bookstorePlace.setPath("bookstore");
    bookstorePlace.setAnimation(Animation.SLIDE);
    bookstorePlace.setWidget(bookstore);
    bookstorePlace.setWelcomePlace(recommendedPlace);
    bookstorePlace.addChild(favoritesPlace);
    bookstorePlace.addChild(discountedPlace);
    bookstorePlace.addChild(mostViewedPlace);
    bookstorePlace.addChild(categorizedPlace);

    recommendedPlace.setPath("recommended");
    recommendedPlace.setAnimation(Animation.SLIDE);
    recommendedPlace.setWidget(recommended);
    recommendedPlace.setTitle("精品推荐");
    recommendedPlace.setButtonText("推荐");
    recommendedPlace.setButtonImage(bundle.tabBarFeaturedImage());

    favoritesPlace.setPath("favorites");
    favoritesPlace.setAnimation(Animation.SLIDE);
    favoritesPlace.setWidget(favorites);
    favoritesPlace.setTitle("我的收藏");
    favoritesPlace.setButtonText("收藏");
    favoritesPlace.setButtonImage(bundle.tabBarFavoritesImage());

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

    appPlace.setPath("app");
    appPlace.setAnimation(Animation.SLIDE);
    appPlace.setTitle("应用控制台");
    appPlace.setWidget(appLayout);
    appPlace.addChild(booksPlace);
    appPlace.addChild(magazinesPlace);
    appPlace.addChild(newspapersPlace);

    booksPlace.setPath("books");
    booksPlace.setAnimation(Animation.SLIDE);
    booksPlace.setTitle("图书");

    magazinesPlace.setPath("magazines");
    magazinesPlace.setAnimation(Animation.SLIDE);
    magazinesPlace.setTitle("杂志");

    newspapersPlace.setPath("newspapers");
    newspapersPlace.setAnimation(Animation.SLIDE);
    newspapersPlace.setTitle("报刊");
  }
}