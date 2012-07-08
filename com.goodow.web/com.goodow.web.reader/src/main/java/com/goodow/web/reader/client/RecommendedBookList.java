package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.touch.TouchPanel;

import java.util.ArrayList;
import java.util.List;

public class RecommendedBookList extends BookListView {

  TouchPanel container;

  private final Provider<BookSummary> bookViewProvider;

  @Inject
  public RecommendedBookList(final Provider<BookSummary> bookViewProvider) {
    this.bookViewProvider = bookViewProvider;
    title.setText("精品推荐");
    container = new TouchPanel();
    for (Book book : createBooks()) {
      BookSummary view = bookViewProvider.get();
      view.setBook(book);
      container.add(view);
    }

    scrollPanel.setWidget(container);

    if (MGWT.getOsDetection().isDesktop()) {
      Window.addResizeHandler(new ResizeHandler() {

        @Override
        public void onResize(final ResizeEvent event) {
          Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
              adjustSize();

            }
          });

        }
      });
    }
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
    container.getElement().getStyle().clearHeight();
    int scrollHeight = container.getElement().getOffsetHeight();
    container.setHeight(scrollHeight + "px");
    scrollPanel.removeFromParent();
    main.add(scrollPanel);
  }

  private List<Book> createBooks() {
    List<Book> result = new ArrayList<Book>();
    for (int i = 0; i < 50; i++) {
      Book book = new Book();
      book.setTitle("小城三月" + (i + 1));
      book.setDescription("小城三月的故事");
      book.setAuthor("作者" + (i + 1));
      result.add(book);
    }
    return result;
  }
}
