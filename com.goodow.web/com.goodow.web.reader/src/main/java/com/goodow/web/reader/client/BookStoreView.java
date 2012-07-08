package com.goodow.web.reader.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.inject.Inject;

import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;
import com.googlecode.mgwt.ui.client.widget.tabbar.BookmarkTabBarButton;
import com.googlecode.mgwt.ui.client.widget.tabbar.FavoritesTabBarButton;
import com.googlecode.mgwt.ui.client.widget.tabbar.HistoryTabBarButton;
import com.googlecode.mgwt.ui.client.widget.tabbar.MostViewedTabBarButton;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;

public class BookStoreView extends Composite {

  protected LayoutPanel main;

  protected HeaderPanel headerPanel;
  protected HeaderButton headerBackButton;
  protected HeaderButton headerMainButton;

  TabPanel tabPanel;

  @Inject
  public BookStoreView(final RecommendedBookList recommendedBook,
      final DiscountBookList discountedBooks, final MostViewedBookList mostViewed,
      final CategorizedBookList categorized) {
    main = new LayoutPanel();
    tabPanel = new TabPanel();

    FavoritesTabBarButton favorites = new FavoritesTabBarButton();
    favorites.setText("推荐");
    tabPanel.add(favorites, recommendedBook);

    HistoryTabBarButton recent = new HistoryTabBarButton();
    recent.setText("特价");
    tabPanel.add(recent, discountedBooks);

    MostViewedTabBarButton history = new MostViewedTabBarButton();
    history.setText("热门");
    tabPanel.add(history, mostViewed);

    BookmarkTabBarButton bookshelf = new BookmarkTabBarButton();
    bookshelf.setText("分类");
    tabPanel.add(bookshelf, categorized);

    main.add(tabPanel);

    initWidget(main);
  }
}
