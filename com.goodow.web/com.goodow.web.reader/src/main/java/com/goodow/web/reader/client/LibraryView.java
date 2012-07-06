package com.goodow.web.reader.client;

import com.goodow.web.core.client.DetailView;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

import com.googlecode.mgwt.ui.client.widget.buttonbar.ActionButton;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBarSpacer;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBarText;
import com.googlecode.mgwt.ui.client.widget.buttonbar.TrashButton;
import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollEndEvent;

public class LibraryView extends DetailView {

  HTMLPanel container;

  HTML html;

  public LibraryView() {
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

    footerPanel.add(new ActionButton());
    footerPanel.add(new ButtonBarSpacer());
    footerPanel.add(new ButtonBarText("there are much more buttons"));
    footerPanel.add(new ButtonBarSpacer());
    // footerPanel.add(new ActionButton());
    // footerPanel.add(new ArrowDownButton());
    // footerPanel.add(new ArrowUpButton());
    // footerPanel.add(new ArrowLeftButton());
    // footerPanel.add(new ArrowRightButton());
    // footerPanel.add(new BookmarkButton());
    // footerPanel.add(new CameraButton());
    // footerPanel.add(new ComposeButton());
    //
    // footerPanel.add(new FastForwardButton());
    //
    // footerPanel.add(new InfoButton());
    // footerPanel.add(new LocateButton());
    // footerPanel.add(new NewIconButton());
    // footerPanel.add(new NextSlideButton());
    // footerPanel.add(new OrganizeButton());
    // footerPanel.add(new PauseButton());
    // footerPanel.add(new PlayButton());
    // footerPanel.add(new PlusButton());
    // footerPanel.add(new PreviousSlideButton());
    // footerPanel.add(new ReplyButton());
    // footerPanel.add(new RewindButton());
    // footerPanel.add(new SearchButton());
    // footerPanel.add(new StopButton());
    footerPanel.add(new TrashButton());
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
