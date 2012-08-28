package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebPlace;
import com.goodow.web.reader.client.BookEditor;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.mvp.client.Animation;

public class BookPlace extends WebPlace {

  public final WebPlace editBookPlace;

  @Inject
  public BookPlace(final WebPlace editBookPlace, final Provider<BookEditor> contentEditor) {

    this.editBookPlace = editBookPlace;

    addChild(editBookPlace);

    editBookPlace.setPath("edit");
    editBookPlace.setAnimation(Animation.SLIDE);
    editBookPlace.setTitle("编辑图书");
    editBookPlace.setWidget(contentEditor);
  }

  @Override
  public String getTitle() {
    if (getPath() == null) {
      return "制作新书";
    } else {
      return "图书";
    }
  }
}
