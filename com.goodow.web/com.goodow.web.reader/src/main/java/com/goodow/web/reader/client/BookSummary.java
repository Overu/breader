package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class BookSummary extends Composite {

  interface Binder extends UiBinder<Widget, BookSummary> {
  }

  private static Binder uiBinder = GWT.create(Binder.class);

  @UiField(provided = true)
  Image bookImage;

  @UiField
  HTMLPanel rightPanel;

  @UiField
  Label titleLabel;

  @UiField
  Label authorLabel;

  @UiField
  Label descLabel;

  private Book book;

  @Inject
  public BookSummary() {
    bookImage = new Image();
    Widget widget = uiBinder.createAndBindUi(this);
    initWidget(widget);
  }

  public void refresh() {
    if (book == null) {
      return;
    } else {
      titleLabel.setText(book.getTitle());
      authorLabel.setText(book.getAuthor());
      descLabel.setText(book.getDescription());
    }
  }

  public void setBook(final Book book) {
    this.book = book;
    refresh();
  }
}
