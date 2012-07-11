package com.goodow.web.reader.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.inject.Inject;

public class BookStoreView extends Composite {

  @Inject
  public BookStoreView(final RecommendedBookList recommended, final DiscountBookList discounted,
      final MostViewedBookList mostViewed, final CategorizedBookList categorized,
      final ReadTabPanel readTabPanel) {
    readTabPanel.add(recommended);
    readTabPanel.add(discounted);
    readTabPanel.add(mostViewed);
    readTabPanel.add(categorized);
    initWidget(readTabPanel);
  }
}
