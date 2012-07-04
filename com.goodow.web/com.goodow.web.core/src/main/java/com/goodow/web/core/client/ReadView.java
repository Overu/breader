package com.goodow.web.core.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollEndEvent;

public class ReadView extends DetailView {

  HTMLPanel container;

  HTML html;

  public ReadView() {
    main.ensureDebugId("read-main");
    container = new HTMLPanel("");

    html = new HTML();

    String content = "";
    for (int i = 0; i < 1000; i++) {
      content += " a b c " + i;
    }
    html.setHTML(content);

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

  private void adjustSize() {
    int columnWidth = main.getOffsetWidth();
    int columnHeight = scrollPanel.getOffsetHeight();
    container.setHeight(columnHeight + "px");
    container.getElement().getStyle().setProperty("webkitColumnWidth", columnWidth + "px");
    container.getElement().getStyle().setProperty("webkitColumnGap", "0px");
    int scrollWidth = container.getElement().getScrollWidth();
    container.setWidth(scrollWidth + "px");
    container.removeFromParent();
    scrollPanel.setWidget(container);
  }
}
