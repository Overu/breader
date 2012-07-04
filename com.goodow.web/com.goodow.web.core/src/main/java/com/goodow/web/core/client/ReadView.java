package com.goodow.web.core.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

import com.googlecode.mgwt.ui.client.widget.Button;

public class ReadView extends DetailView {

  HTMLPanel container;

  Button computeBtn;

  HTML html;

  public ReadView() {

    // scrollPanel.removeFromParent();

    // carousel = new Carousel();
    // carousel.ensureDebugId("read-carousel");

    // main.add(carousel);
    main.ensureDebugId("read-main");
    container = new HTMLPanel("");

    html = new HTML();

    String content = "";
    for (int i = 0; i < 1000; i++) {
      content += " a b c " + i;
    }
    html.setHTML(content);

    computeBtn = new Button("Compute");
    // main.add(computeBtn);
    // computeBtn.addTouchHandler(new TouchHandler() {
    //
    // @Override
    // public void onTouchCanceled(final TouchCancelEvent event) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onTouchEnd(final TouchEndEvent event) {
    // int w = container.getElement().getScrollWidth();
    // Window.alert("Width=" + w);
    // }
    //
    // @Override
    // public void onTouchMove(final TouchMoveEvent event) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onTouchStart(final TouchStartEvent event) {
    //
    // }
    // });

    scrollPanel.setSnap(true);
    scrollPanel.setMomentum(false);
    scrollPanel.setShowScrollBarX(false);
    scrollPanel.setShowScrollBarY(false);
    scrollPanel.setScrollingEnabledX(true);
    scrollPanel.setScrollingEnabledY(false);

    scrollPanel.setAutoHandleResize(true);

    // scrollPanel.addScrollEndHandler(new ScrollEndEvent.Handler() {
    //
    // @Override
    // public void onScrollEnd(final ScrollEndEvent event) {
    //
    // System.out.println("x=" + scrollPanel.getCurrentPageX());
    // System.out.println("y=" + scrollPanel.getCurrentPageY());
    // }
    // });
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
    // container.setWidth(100000 + "px");
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

      @Override
      public void execute() {
        adjustSize();
      }
    });
  }

  private void adjustSize() {
    int gap = 0;
    int columnWidth = main.getOffsetWidth() - gap;
    int columnHeight = scrollPanel.getOffsetHeight();
    container.setHeight(columnHeight + "px");
    // container.getElement().getStyle().setMargin(5, Unit.PX);
    container.getElement().getStyle().setProperty("webkitColumnWidth", columnWidth + "px");
    container.getElement().getStyle().setProperty("webkitColumnGap", gap + "px");
    int scrollWidth = container.getElement().getScrollWidth();
    container.setWidth(scrollWidth + "px");
    container.removeFromParent();
    scrollPanel.setWidget(container);
    // Scheduler.get().scheduleDeferred(new ScheduledCommand() {
    // @Override
    // public void execute() {
    // int scrollWidth = container.getElement().getScrollWidth();
    // container.setWidth(scrollWidth + "px");
    // }
    // });
  }
}
