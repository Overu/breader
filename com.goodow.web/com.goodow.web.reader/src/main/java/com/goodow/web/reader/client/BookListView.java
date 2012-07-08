package com.goodow.web.reader.client;

import com.goodow.web.core.shared.MyPlace;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.googlecode.mgwt.dom.client.event.touch.TouchCancelEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;
import com.googlecode.mgwt.mvp.client.Animation;
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

  @Inject
  protected PlaceController placeController;

  public BookListView() {
    main = new LayoutPanel();

    scrollPanel = new ScrollPanel();

    headerPanel = new HeaderPanel();
    title = new HTML();

    headerPanel.setCenterWidget(title);

    rightButton = new HeaderButton();
    rightButton.setRoundButton(true);
    rightButton.setText("书架");
    rightButton.addTouchHandler(new TouchHandler() {

      @Override
      public void onTouchCanceled(final TouchCancelEvent event) {
        // TODO Auto-generated method stub
      }

      @Override
      public void onTouchEnd(final TouchEndEvent event) {
        MyPlace place = new MyPlace(Animation.SWAP, "书架", getRightLink());
        placeController.goTo(place);
      }

      @Override
      public void onTouchMove(final TouchMoveEvent event) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onTouchStart(final TouchStartEvent event) {
        // TODO Auto-generated method stub

      }
    });

    leftButton = new HeaderButton();
    leftButton.setRoundButton(true);
    leftButton.setText("设置");

    headerPanel.setLeftWidget(leftButton);
    headerPanel.setRightWidget(rightButton);

    main.add(headerPanel);
    main.add(scrollPanel);
    initWidget(main);
  }

  protected String getRightLink() {
    return "/bookshelf";
  }

}
