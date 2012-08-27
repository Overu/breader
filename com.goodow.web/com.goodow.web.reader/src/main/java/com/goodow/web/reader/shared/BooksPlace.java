package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebPlace;
import com.goodow.web.reader.client.BooksBrowser;
import com.goodow.web.reader.client.MyBookList;
import com.goodow.web.reader.client.SelectedBookList;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.mvp.client.Animation;

public class BooksPlace extends WebPlace {

  public final BookPlace bookPlace;

  public final WebPlace myBooksPlace;

  public final WebPlace othersBookPlace;

  public final WebPlace selectedBooksPlace;

  public final WebPlace catogrizedPlace;

  @Inject
  public BooksPlace(final BookPlace bookPlace, final WebPlace myBooksPlace,
      final WebPlace othersBookPlace, final WebPlace selectedBooksPlace,
      final WebPlace catogrizedPlace, final Provider<BooksBrowser> booksBrowser,
      final Provider<MyBookList> myBookList, final Provider<SelectedBookList> selectedBookList) {

    this.bookPlace = bookPlace;
    this.myBooksPlace = myBooksPlace;
    this.othersBookPlace = othersBookPlace;
    this.selectedBooksPlace = selectedBooksPlace;
    this.catogrizedPlace = catogrizedPlace;

    setPath("books");
    setFeed(true);
    setEntryPlace(bookPlace);

    setAnimation(Animation.SLIDE);
    setWidget(booksBrowser);

    setWelcomePlace(myBooksPlace);
    addChild(othersBookPlace);
    addChild(selectedBooksPlace);
    addChild(catogrizedPlace);

    myBooksPlace.setQuery("filter=my");
    myBooksPlace.setAnimation(Animation.SLIDE);
    myBooksPlace.setTitle("我的图书");
    myBooksPlace.setWidget(myBookList);

    othersBookPlace.setQuery("filter=others");
    othersBookPlace.setAnimation(Animation.SLIDE);
    othersBookPlace.setTitle("其他图书");

    selectedBooksPlace.setQuery("filter=selected");
    selectedBooksPlace.setAnimation(Animation.SLIDE);
    selectedBooksPlace.setTitle("精品图书");
    selectedBooksPlace.setWidget(selectedBookList);

    catogrizedPlace.setQuery("filter=category");
    catogrizedPlace.setAnimation(Animation.SLIDE);
    catogrizedPlace.setTitle("图书分类");
  }

}
