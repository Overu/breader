package com.goodow.web.reader.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

public abstract class BookListView extends Composite {

  protected LayoutPanel main;

  protected HeaderPanel headerPanel;
  protected HeaderButton rightButton;
  protected HeaderButton leftButton;
  protected HTML title;

  protected ScrollPanel scrollPanel;
  protected HTMLPanel content;

  @Inject
  protected PlaceController placeController;

  @Inject
  protected ReaderPlugin readerExtension;

  public BookListView() {
    main = new LayoutPanel();

    scrollPanel = new ScrollPanel();
    content = new HTMLPanel("");

    headerPanel = new HeaderPanel();
    title = new HTML();

    headerPanel.setCenterWidget(title);

    rightButton = new HeaderButton();
    rightButton.setRoundButton(true);
    rightButton.setText("书架");
    rightButton.addTapHandler(new TapHandler() {
      @Override
      public void onTap(final TapEvent event) {
        placeController.goTo(readerExtension.bookstorePlace);
      }
    });

    leftButton = new HeaderButton();
    leftButton.setRoundButton(true);
    leftButton.setText("设置");

    headerPanel.setLeftWidget(leftButton);
    headerPanel.setRightWidget(rightButton);

    main.add(headerPanel);
    // main.add(scrollPanel);
    content.add(scrollPanel);
    main.add(content);
    initWidget(main);
  }

}
