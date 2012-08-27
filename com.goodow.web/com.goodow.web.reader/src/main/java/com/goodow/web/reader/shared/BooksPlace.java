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

  public final WebPlace bookcategoryPlace;

  @Inject
  public BooksPlace(final BookPlace bookPlace, final WebPlace myBooksPlace,
      final WebPlace othersBookPlace, final WebPlace selectedBooksPlace,
      final WebPlace bookcategoryPlace, final Provider<BooksBrowser> booksBrowser,
      final Provider<MyBookList> myBookList, final Provider<SelectedBookList> selectedBookList) {

    this.bookPlace = bookPlace;
    this.myBooksPlace = myBooksPlace;
    this.othersBookPlace = othersBookPlace;
    this.selectedBooksPlace = selectedBooksPlace;
    this.bookcategoryPlace = bookcategoryPlace;

    setPath("books");
    setAnimation(Animation.SLIDE);
    setWidget(booksBrowser);

    addChild(bookPlace);
    addChild(myBooksPlace);
    addChild(othersBookPlace);
    addChild(selectedBooksPlace);
    addChild(bookcategoryPlace);

    myBooksPlace.setPath("my");
    myBooksPlace.setAnimation(Animation.SLIDE);
    myBooksPlace.setTitle("我的图书");
    myBooksPlace.setWidget(myBookList);

    othersBookPlace.setPath("others");
    othersBookPlace.setAnimation(Animation.SLIDE);
    othersBookPlace.setTitle("其他图书");

    selectedBooksPlace.setPath("selected");
    selectedBooksPlace.setAnimation(Animation.SLIDE);
    selectedBooksPlace.setTitle("精品图书");
    selectedBooksPlace.setWidget(selectedBookList);

    bookcategoryPlace.setPath("category");
    bookcategoryPlace.setAnimation(Animation.SLIDE);
    bookcategoryPlace.setTitle("图书分类");
  }

}
