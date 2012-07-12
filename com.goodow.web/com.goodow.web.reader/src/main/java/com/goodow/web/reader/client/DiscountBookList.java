package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.util.CssUtil;
import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollAnimationMoveEvent;
import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class DiscountBookList extends AbstractBookList {

  ReadFixedHeadPanel rfhPanel;

  SimplePanel staticPanel;

  private boolean headerVisible = true;

  @Inject
  public DiscountBookList(final Provider<BookSummary> bookSummaryProvider) {
    super(bookSummaryProvider);

    staticPanel = new SimplePanel();

    setTitle("特价促销");
    bookList.removeFromParent();
    rfhPanel = new ReadFixedHeadPanel();
    rfhPanel.addButton("最新特价");
    rfhPanel.addButton("免费专区");

    container.add(staticPanel);
    container.add(rfhPanel);
    container.add(bookList);

    this.addScrollMoveHandler(new ScrollMoveEvent.Handler() {

      @Override
      public void onScrollMove(final ScrollMoveEvent event) {
        updateHeaderPositionAndTitle(DiscountBookList.this.getY());
      }

    });

    this.addScrollAnimationMoveHandler(new ScrollAnimationMoveEvent.Handler() {

      @Override
      public void onScrollAnimationMove(final ScrollAnimationMoveEvent event) {
        updateHeaderPositionAndTitle(DiscountBookList.this.getY());

      }
    });
  }

  protected void adjustSize() {
    staticPanel.setHeight(rfhPanel.getOffsetHeight() + "px");
    Style rfhCss = rfhPanel.getElement().getStyle();
    rfhCss.setLeft(0, Unit.PX);
    rfhCss.setRight(0, Unit.PX);
    if (getY() == 0 && headerVisible) {
      staticPanel.setVisible(false);
      headerVisible = false;
    }
  }

  @Override
  protected List<Book> createBooks() {
    List<Book> result = new ArrayList<Book>();
    for (int i = 0; i < 40; i++) {
      Book book = new Book();
      book.setTitle("小城三月" + (i + 1));
      book.setDescription("小城三月的故事");
      book.setAuthor("作者" + (i + 1));
      result.add(book);
    }
    return result;
  }

  @Override
  protected ImageResource getButtonImage() {
    return MGWTStyle.getTheme().getMGWTClientBundle().tabBarHistoryImage();
  }

  @Override
  protected String getButtonText() {
    return "特价";
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
