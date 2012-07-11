package com.goodow.web.reader.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.inject.Inject;

import com.googlecode.mgwt.ui.client.MGWTStyle;

public class BookStoreView extends Composite {

  @Inject
  public BookStoreView(final RecommendedBookList recommended, final DiscountBookList discounted,
      final MostViewedBookList mostViewed, final CategorizedBookList categorized,
      final ReadTabPanel readTabPanel) {

    readTabPanel.add(recommended, "精品推荐", "推荐", MGWTStyle.getTheme().getMGWTClientBundle()
        .tabBarFavoritesImage());

    readTabPanel.add(discounted, "特价促销", "特价", MGWTStyle.getTheme().getMGWTClientBundle()
        .tabBarHistoryImage());

    readTabPanel.add(mostViewed, "热门图书", "热门", MGWTStyle.getTheme().getMGWTClientBundle()
        .tabBarMostViewedImage());

    readTabPanel.add(categorized, "分类浏览", "分类", MGWTStyle.getTheme().getMGWTClientBundle()
        .tabBarBookMarkImage());

    initWidget(readTabPanel);
  }
}
