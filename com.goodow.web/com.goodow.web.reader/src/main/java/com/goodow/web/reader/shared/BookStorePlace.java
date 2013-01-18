package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebPlace;
import com.goodow.web.reader.client.BookStore;
import com.goodow.web.reader.client.CategorizedBookList;
import com.goodow.web.reader.client.DiscountBookList;
import com.goodow.web.reader.client.FavoriteBooks;
import com.goodow.web.reader.client.MostViewedBookList;
import com.goodow.web.reader.client.RecommendedBookList;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.MGWTClientBundle;

@Singleton
public class BookStorePlace extends WebPlace {

  public final WebPlace recommendedPlace;

  public final WebPlace favoritesPlace;

  public final WebPlace discountedPlace;

  public final WebPlace mostViewedPlace;

  public final WebPlace categorizedPlace;
  
  @Inject
  public BookStorePlace(final WebPlace recommendedPlace, final WebPlace favoritesPlace,
      final WebPlace discountedPlace, final WebPlace mostViewedPlace,
      final WebPlace categorizedPlace, final Provider<BookStore> bookstore,
      final Provider<RecommendedBookList> recommended, final Provider<FavoriteBooks> favorites,
      final Provider<DiscountBookList> discounted, final Provider<MostViewedBookList> mostViewed,
      final Provider<CategorizedBookList> categorized) {

    this.recommendedPlace = recommendedPlace;
    this.favoritesPlace = favoritesPlace;
    this.discountedPlace = discountedPlace;
    this.mostViewedPlace = mostViewedPlace;
    this.categorizedPlace = categorizedPlace;

    MGWTClientBundle bundle = MGWTStyle.getTheme().getMGWTClientBundle();
    setPath("bookstore");
    setWidget(bookstore);
    setWelcomePlace(recommendedPlace);
    addChild(favoritesPlace);
    addChild(discountedPlace);
    addChild(mostViewedPlace);
    addChild(categorizedPlace);

    recommendedPlace.setPath("recommended");
    recommendedPlace.setWidget(recommended);
    recommendedPlace.setTitle("精品推荐");
    recommendedPlace.setButtonText("推荐");
    recommendedPlace.setButtonImage(bundle.tabBarFeaturedImage());

    favoritesPlace.setPath("favorites");
    favoritesPlace.setWidget(favorites);
    favoritesPlace.setTitle("我的收藏");
    favoritesPlace.setButtonText("收藏");
    favoritesPlace.setButtonImage(bundle.tabBarFavoritesImage());

    discountedPlace.setPath("discounted");
    discountedPlace.setWidget(discounted);
    discountedPlace.setTitle("特价促销");
    discountedPlace.setButtonText("特价");
    discountedPlace.setButtonImage(bundle.tabBarHistoryImage());

    mostViewedPlace.setPath("mostviewed");
    mostViewedPlace.setWidget(mostViewed);
    mostViewedPlace.setTitle("热门图书");
    mostViewedPlace.setButtonText("热门");
    mostViewedPlace.setButtonImage(bundle.tabBarMostViewedImage());

    categorizedPlace.setPath("categorized");
    categorizedPlace.setWidget(categorized);
    categorizedPlace.setTitle("分类浏览");
    categorizedPlace.setButtonText("分类");
    categorizedPlace.setButtonImage(bundle.tabBarBookMarkImage());
  }

}
