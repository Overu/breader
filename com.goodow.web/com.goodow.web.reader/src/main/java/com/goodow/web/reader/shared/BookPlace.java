package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebPlace;
import com.goodow.web.reader.client.BookEditor;
import com.goodow.web.reader.client.BookForm;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.mvp.client.Animation;

public class BookPlace extends WebPlace {

  public final WebPlace createBookPlace;

  public final WebPlace editBookPlace;

  @Inject
  public BookPlace(final WebPlace createBookPlace, final WebPlace editBookPlace,
      final Provider<BookForm> bookForm, final Provider<BookEditor> contentEditor) {

    setTitle("图书");

    this.createBookPlace = createBookPlace;
    this.editBookPlace = editBookPlace;

    addChild(editBookPlace);
    addChild(createBookPlace);

    createBookPlace.setPath("create");
    createBookPlace.setAnimation(Animation.SLIDE);
    createBookPlace.setTitle("制作新书");
    createBookPlace.setWidget(bookForm);

    editBookPlace.setPath("edit");
    editBookPlace.setAnimation(Animation.SLIDE);
    editBookPlace.setTitle("编辑图书");
    editBookPlace.setWidget(contentEditor);
  }
}
