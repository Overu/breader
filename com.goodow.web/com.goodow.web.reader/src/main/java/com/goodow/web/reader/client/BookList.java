package com.goodow.web.reader.client;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.inject.Inject;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.googlecode.mgwt.ui.client.widget.buttonbar.RefreshButton;
import com.googlecode.mgwt.ui.client.widget.buttonbar.TrashButton;

import java.util.List;

public class BookList extends FlowView implements Receiver<List<Book>> {

  @Inject
  private ScrollPanel scrollPanel;

  private CellList<Book> cellListWithHeader;

  @Inject
  ButtonBar buttonBar;

  @Inject
  RefreshButton refreshButton;

  @Inject
  TrashButton deleteButton;

  @Inject
  AsyncBookService bookService;

  public void delete() {
    // TODO find selected books
    Book book = null;
    bookService.remove(book).fire(new Receiver<Void>() {
      @Override
      public void onSuccess(final Void result) {
        refresh();
      }
    });
  }

  @Override
  public void onSuccess(final List<Book> result) {
    cellListWithHeader.render(result);
  }

  @Override
  protected void start() {

    cellListWithHeader = new CellList<Book>(new BookCell<Book>() {

      @Override
      public boolean canBeSelected(final Book model) {
        return true;
      }

      @Override
      public String getDisplayString(final Book model) {
        return model.getTitle() + "  - " + model.getDescription();
      }
    });

    cellListWithHeader.setRound(true);
    scrollPanel.setWidget(cellListWithHeader);
    scrollPanel.setScrollingEnabledX(false);

    buttonBar.add(refreshButton);
    buttonBar.add(deleteButton);

    main.add(scrollPanel);

    main.add(buttonBar);

    refreshButton.addTapHandler(new TapHandler() {

      @Override
      public void onTap(final TapEvent event) {
        refresh();
      }
    });

    deleteButton.addTapHandler(new TapHandler() {

      @Override
      public void onTap(final TapEvent event) {
        delete();
      }
    });

    refresh();
  }
}
