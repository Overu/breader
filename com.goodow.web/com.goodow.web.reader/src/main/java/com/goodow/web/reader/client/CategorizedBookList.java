package com.goodow.web.reader.client;

import com.goodow.web.reader.client.style.ReadResources;
import com.goodow.web.reader.shared.Book;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class CategorizedBookList extends AbstractBookList {

  CategoryListView categoryListView;

  @Inject
  public CategorizedBookList(final Provider<BookSummary> bookSummaryProvider) {
    super(bookSummaryProvider);

    setScrollingEnabledX(false);
    setScrollingEnabledY(false);
    setShowScrollBarX(false);
    setShowScrollBarY(false);

    container.addStyleName(ReadResources.INSTANCE().categroyListCss().categorContainer());

    categoryListView = new CategoryListView();
    categoryListView.addScrollMoveHandler(new ScrollMoveEvent.Handler() {

      @Override
      public void onScrollMove(final ScrollMoveEvent event) {
        event.getEvent().stopPropagation();
      }
    });

    bookList.removeFromParent();
    ScrollPanel scrollPanel2 = new ScrollPanel();
    scrollPanel2.setWidget(bookList);
    scrollPanel2.setScrollingEnabledX(false);
    scrollPanel2.setScrollingEnabledY(true);
    scrollPanel2.addScrollMoveHandler(new ScrollMoveEvent.Handler() {

      @Override
      public void onScrollMove(final ScrollMoveEvent event) {
        event.getEvent().stopPropagation();
      }
    });

    container.add(categoryListView);
    container.add(scrollPanel2);
  }

  @Override
  public String setBookSummaryLandscapeCss() {
    return BookSummary.INSTANCE.landscape().root1();
  }

  @Override
  protected List<Book> createBooks() {
    List<Book> result = new ArrayList<Book>();
    for (int i = 0; i < 40; i++) {
      Book book = new Book();
      book.setTitle("小城三月" + (i + 1));
      book.setDescription("小城三月的故事");
      book.setAuthor("作者" + (i + 1));
      result.add(book);
    }
    return result;
  }

}