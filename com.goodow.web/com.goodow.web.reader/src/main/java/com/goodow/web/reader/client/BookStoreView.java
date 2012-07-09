package com.goodow.web.reader.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.inject.Inject;

import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.tabbar.BookmarkTabBarButton;
import com.googlecode.mgwt.ui.client.widget.tabbar.FavoritesTabBarButton;
import com.googlecode.mgwt.ui.client.widget.tabbar.HistoryTabBarButton;
import com.googlecode.mgwt.ui.client.widget.tabbar.MostViewedTabBarButton;

public class BookStoreView extends Composite {

  private ReadTabPanel readTabPanel;

  @Inject
  public BookStoreView(final RecommendedBookList recommendedBook,
      final DiscountBookList discountedBooks, final MostViewedBookList mostViewed,
      final CategorizedBookList categorized) {

    readTabPanel = new ReadTabPanel();

    FavoritesTabBarButton favorites = new FavoritesTabBarButton();
    favorites.setText("推荐");
    ScrollPanel scrollPanel = new ScrollPanel();
    scrollPanel.setWidget(recommendedBook);
    readTabPanel.add(favorites, scrollPanel, "精品推荐");

    HistoryTabBarButton recent = new HistoryTabBarButton();
    recent.setText("特价");
    readTabPanel.add(recent, discountedBooks, "特价促销");

    MostViewedTabBarButton history = new MostViewedTabBarButton();
    history.setText("热门");
    readTabPanel.add(history, mostViewed, "热门图书");

    BookmarkTabBarButton bookshelf = new BookmarkTabBarButton();
    bookshelf.setText("分类");
    readTabPanel.add(bookshelf, categorized, "分类浏览");

    initWidget(readTabPanel);
  }
}
