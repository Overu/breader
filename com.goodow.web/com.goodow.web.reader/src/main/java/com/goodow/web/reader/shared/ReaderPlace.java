package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebPlace;
import com.goodow.web.reader.client.ReaderApp;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.googlecode.mgwt.mvp.client.Animation;

@Singleton
public class ReaderPlace extends WebPlace {

  public final BooksPlace booksPlace;
  public final BookStorePlace bookstorePlace;
  public final BookShelfPlace bookshelfPlace;

  @Inject
  public ReaderPlace(final BookStorePlace bookstorePlace, final BookShelfPlace bookshelfPlace,
      final BooksPlace booksPlace, final Provider<ReaderApp> readerApp) {

    this.bookstorePlace = bookstorePlace;
    this.bookshelfPlace = bookshelfPlace;
    this.booksPlace = booksPlace;

    setWelcomePlace(booksPlace);
    addChild(bookstorePlace);
    addChild(bookshelfPlace);

    setAnimation(Animation.FLIP);
    setWidget(readerApp);
    setTitle("应用控制台");
  }
}