package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Shared;
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

  @Shared
  interface BookSummaryStyle extends CssResource {
    String root();

  }

  interface Bundle extends ClientBundle {
    @Source("bookSummary.portrait.css")
    BookSummaryStyle protrait();
  }

  private static Binder uiBinder = GWT.create(Binder.class);
  private static Bundle INSTANCE;

  static {
    INSTANCE = GWT.create(Bundle.class);
    String portraitCss =
        "@media only screen and (orientation:portrait) {" + INSTANCE.protrait().getText() + "}";
    StyleInjector.injectAtEnd(portraitCss);
  }

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

  @UiField
  DivElement discountedPrice;
  @UiField
  DivElement originalPrice;

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
      setFree();
    }
  }

  public void setBook(final Book book) {
    this.book = book;
    refresh();
  }

  public void setFree() {
    discountedPrice.getStyle().setDisplay(Display.NONE);
    originalPrice.setInnerHTML("免费");
    originalPrice.getStyle().setColor("#FF8500");
  }

  public void setOriginalPrice(final String price) {
    discountedPrice.getStyle().setDisplay(Display.NONE);
    originalPrice.setInnerHTML("$ " + price);
  }

  public void setPrice(final String originalPriceText, final String discountedPriceText) {
    discountedPrice.getStyle().clearDisplay();
    discountedPrice.setInnerHTML("$ " + discountedPriceText);
    originalPrice.setInnerHTML("$ " + originalPriceText);
  }
}
