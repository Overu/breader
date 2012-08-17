package com.goodow.web.reader.client;

import com.goodow.web.core.shared.HomePlace;
import com.goodow.web.core.shared.WebPlace;
import com.goodow.web.reader.client.droppable.CellTableSample;

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
  Provider<BooksApp> booksApp;

  @Inject
  Provider<BookForm> bookForm;

  @Inject
  Provider<BooksView> booksView;

  @Inject
  Provider<MyBookList> myBookList;

  @Inject
  Provider<SelectedBookList> selectedBookList;

  @Inject
  Provider<CellTableSample> cellTableSample;

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
  WebPlace booksPlace;

  @Inject
  WebPlace createBookPlace;

  @Inject
  WebPlace myBooksPlace;

  @Inject
  WebPlace othersBookPlace;

  @Inject
  WebPlace selectedBooksPlace;

  @Inject
  WebPlace bookcategoryPlace;

  @Inject
  WebPlace newBookPlace;

  @Inject
  WebPlace booksViewPlace;

  @Inject
  WebPlace editBookPlace;

  @Inject
  WebPlace cellTablePlace;

  @Inject
  Provider<BookEditor> contentEditor;

  public void start() {

    MGWTClientBundle bundle = MGWTStyle.getTheme().getMGWTClientBundle();

    homePlace.setAnimation(Animation.FLIP);
    homePlace.setWelcomePlace(bookshelfPlace);
    homePlace.addChild(bookstorePlace);
    homePlace.addChild(booksPlace);
    homePlace.addChild(newBookPlace);

    bookshelfPlace.setPattern("bookshelf");
    bookshelfPlace.setAnimation(Animation.SLIDE);
    bookshelfPlace.setWidget(bookshelf);

    bookstorePlace.setPattern("bookstore");
    bookstorePlace.setAnimation(Animation.SLIDE);
    bookstorePlace.setWidget(bookstore);
    bookstorePlace.setWelcomePlace(recommendedPlace);
    bookstorePlace.addChild(favoritesPlace);
    bookstorePlace.addChild(discountedPlace);
    bookstorePlace.addChild(mostViewedPlace);
    bookstorePlace.addChild(categorizedPlace);

    recommendedPlace.setPattern("recommended");
    recommendedPlace.setAnimation(Animation.SLIDE);
    recommendedPlace.setWidget(recommended);
    recommendedPlace.setTitle("精品推荐");
    recommendedPlace.setButtonText("推荐");
    recommendedPlace.setButtonImage(bundle.tabBarFeaturedImage());

    favoritesPlace.setPattern("favorites");
    favoritesPlace.setAnimation(Animation.SLIDE);
    favoritesPlace.setWidget(favorites);
    favoritesPlace.setTitle("我的收藏");
    favoritesPlace.setButtonText("收藏");
    favoritesPlace.setButtonImage(bundle.tabBarFavoritesImage());

    discountedPlace.setPattern("discounted");
    discountedPlace.setAnimation(Animation.SLIDE);
    discountedPlace.setWidget(discounted);
    discountedPlace.setTitle("特价促销");
    discountedPlace.setButtonText("特价");
    discountedPlace.setButtonImage(bundle.tabBarHistoryImage());

    mostViewedPlace.setPattern("mostviewed");
    mostViewedPlace.setAnimation(Animation.SLIDE);
    mostViewedPlace.setWidget(mostViewed);
    mostViewedPlace.setTitle("热门图书");
    mostViewedPlace.setButtonText("热门");
    mostViewedPlace.setButtonImage(bundle.tabBarMostViewedImage());

    categorizedPlace.setPattern("categorized");
    categorizedPlace.setAnimation(Animation.SLIDE);
    categorizedPlace.setWidget(categorized);
    categorizedPlace.setTitle("分类浏览");
    categorizedPlace.setButtonText("分类");
    categorizedPlace.setButtonImage(bundle.tabBarBookMarkImage());

    booksPlace.setPattern("books");
    booksPlace.setAnimation(Animation.SLIDE);
    booksPlace.setTitle("应用控制台");
    booksPlace.setWidget(booksApp);
    booksPlace.setWelcomePlace(booksViewPlace);
    booksPlace.addChild(createBookPlace);
    booksPlace.addChild(myBooksPlace);
    booksPlace.addChild(othersBookPlace);
    booksPlace.addChild(selectedBooksPlace);
    booksPlace.addChild(bookcategoryPlace);
    booksPlace.addChild(editBookPlace);
    booksPlace.addChild(cellTablePlace);

    createBookPlace.setPattern("create");
    createBookPlace.setAnimation(Animation.SLIDE);
    createBookPlace.setTitle("制作新书");
    createBookPlace.setWidget(bookForm);

    myBooksPlace.setPattern("my");
    myBooksPlace.setAnimation(Animation.SLIDE);
    myBooksPlace.setTitle("我的图书");
    myBooksPlace.setWidget(myBookList);

    othersBookPlace.setPattern("others");
    othersBookPlace.setAnimation(Animation.SLIDE);
    othersBookPlace.setTitle("其他图书");

    selectedBooksPlace.setPattern("selected");
    selectedBooksPlace.setAnimation(Animation.SLIDE);
    selectedBooksPlace.setTitle("精品图书");
    selectedBooksPlace.setWidget(selectedBookList);

    bookcategoryPlace.setPattern("category");
    bookcategoryPlace.setAnimation(Animation.SLIDE);
    bookcategoryPlace.setTitle("图书分类");

    booksViewPlace.setPattern("booksview");
    booksViewPlace.setAnimation(Animation.SLIDE);
    booksViewPlace.setWidget(booksView);
    booksViewPlace.setTitle("welcome");

    newBookPlace.setPattern("new");
    newBookPlace.setAnimation(Animation.SLIDE);
    newBookPlace.setTitle("制作新书");
    newBookPlace.setWidget(bookForm);

    editBookPlace.setPattern("{id}");
    editBookPlace.setAnimation(Animation.SLIDE);
    editBookPlace.setTitle("编辑内容");
    editBookPlace.setWidget(contentEditor);

    cellTablePlace.setPattern("cellTable");
    cellTablePlace.setAnimation(Animation.SLIDE);
    cellTablePlace.setTitle("拖拽实验");
    cellTablePlace.setWidget(cellTableSample);
  }
}