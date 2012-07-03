package com.goodow.web.core.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;

import com.googlecode.mgwt.dom.client.event.touch.TouchCancelEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.RoundPanel;
import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollEndEvent;
import com.googlecode.mgwt.ui.client.widget.touch.TouchPanel;

public class ReadView extends DetailView {

  TouchPanel container;

  Button computeBtn;

  public ReadView() {

    // scrollPanel.removeFromParent();

    // carousel = new Carousel();
    // carousel.ensureDebugId("read-carousel");

    // main.add(carousel);
    main.ensureDebugId("read-main");
    container = new RoundPanel();

    HTML html = new HTML();

    String content = "";
    for (int i = 0; i < 100000; i++) {
      content += " hello " + i;
    }
    html.setHTML(content);

    computeBtn = new Button("Compute");
    main.add(computeBtn);
    computeBtn.addTouchHandler(new TouchHandler() {

      @Override
      public void onTouchCanceled(final TouchCancelEvent event) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onTouchEnd(final TouchEndEvent event) {
        int w = container.getElement().getScrollWidth();
        Window.alert("Width=" + w);
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

    scrollPanel.setSnap(true);
    scrollPanel.setMomentum(false);
    scrollPanel.setShowScrollBarX(false);
    scrollPanel.setShowScrollBarY(false);
    scrollPanel.setScrollingEnabledX(true);
    scrollPanel.setScrollingEnabledY(false);

    scrollPanel.setAutoHandleResize(true);

    scrollPanel.addScrollEndHandler(new ScrollEndEvent.Handler() {

      @Override
      public void onScrollEnd(final ScrollEndEvent event) {

        System.out.println("x=" + scrollPanel.getCurrentPageX());
        System.out.println("y=" + scrollPanel.getCurrentPageY());
      }
    });
    container.add(html);
    scrollPanel.setWidget(container);

    // container.setHeight("400px");
    // container.getElement().getStyle().setProperty("webkitColumnWidth", 500 + "px");
    // container.setWidth("10000px");
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Composite#onAttach()
   */
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

  private void adjustSize() {
    int columnWidth = scrollPanel.getOffsetWidth() / 2;
    int columnHeight = scrollPanel.getOffsetHeight();
    container.setHeight(columnHeight + "px");
    container.getElement().getStyle().setProperty("webkitColumnWidth", columnWidth + "px");
    int scrollWidth = container.getElement().getScrollWidth();
    container.setWidth(scrollWidth + "px");
    // Scheduler.get().scheduleDeferred(new ScheduledCommand() {
    // @Override
    // public void execute() {
    // int scrollWidth = container.getElement().getScrollWidth();
    // container.setWidth(scrollWidth + "px");
    // }
    // });
  }
}
