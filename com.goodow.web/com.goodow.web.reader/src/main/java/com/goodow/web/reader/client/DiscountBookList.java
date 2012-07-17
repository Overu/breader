package com.goodow.web.reader.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Singleton;

import com.googlecode.mgwt.ui.client.util.CssUtil;
import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollAnimationMoveEvent;
import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollMoveEvent;

@Singleton
public class DiscountBookList extends AbstractBookList {

  ReadFixedHeadPanel rfhPanel;

  SimplePanel staticPanel;

  private boolean headerVisible = true;

  protected void adjustSize() {
    staticPanel.setHeight(rfhPanel.getOffsetHeight() + "px");
    Style rfhCss = rfhPanel.getElement().getStyle();
    rfhCss.setLeft(0, Unit.PX);
    rfhCss.setRight(0, Unit.PX);
    if (main.getY() == 0 && headerVisible) {
      staticPanel.setVisible(false);
      headerVisible = false;
    }
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

      @Override
      public void execute() {
        adjustSize();
      }
    });
  }

  @Override
  protected void start() {
    super.start();
    staticPanel = new SimplePanel();

    setTitle("特价促销");
    bookList.removeFromParent();
    rfhPanel = new ReadFixedHeadPanel();
    rfhPanel.addButton("最新特价");
    rfhPanel.addButton("免费专区");

    container.add(staticPanel);
    container.add(rfhPanel);
    container.add(bookList);

    main.addScrollMoveHandler(new ScrollMoveEvent.Handler() {

      @Override
      public void onScrollMove(final ScrollMoveEvent event) {
        updateHeaderPositionAndTitle(main.getY());
      }

    });

    main.addScrollAnimationMoveHandler(new ScrollAnimationMoveEvent.Handler() {

      @Override
      public void onScrollAnimationMove(final ScrollAnimationMoveEvent event) {
        updateHeaderPositionAndTitle(main.getY());
      }
    });
  }

  private void updateHeaderPositionAndTitle(final int y) {

    Style rfhCss = rfhPanel.getElement().getStyle();
    int rfhHeight = rfhPanel.getOffsetHeight();
    int move = -rfhHeight - y;

    if (y > 0) {
      if (headerVisible) {
        headerVisible = false;
        rfhCss.clearPosition();
        staticPanel.setVisible(false);
      }
      CssUtil.translate(rfhPanel.getElement(), 0, 0);
    } else {
      if (!headerVisible) {
        headerVisible = true;
        rfhCss.setPosition(Position.ABSOLUTE);
        staticPanel.setVisible(true);
      }
      CssUtil.translate(rfhPanel.getElement(), 0, move);
    }

  }
}
