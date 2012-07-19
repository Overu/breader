package com.goodow.web.reader.client;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.googlecode.mgwt.ui.client.widget.buttonbar.RefreshButton;
import com.googlecode.mgwt.ui.client.widget.buttonbar.TrashButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BookList extends FlowView implements Receiver<List<Book>> {

  @Inject
  private ScrollPanel scrollPanel;

  private CellList<Book> cellListWithHeader;

  private ListDataProvider<Book> dataProvider;

  private int oldIndex;

  Set<Book> books;

  @Inject
  ButtonBar buttonBar;

  @Inject
  RefreshButton refreshButton;

  @Inject
  TrashButton deleteButton;

  @Inject
  AsyncBookService bookService;

  public void delete() {

    if (books == null || books.size() == 0) {
      return;
    }

    for (Book book : books) {
      bookService.remove(book).fire(new Receiver<Void>() {
        @Override
        public void onSuccess(final Void result) {
          refresh();
        }
      });
    }

  }

  @Override
  public void onSuccess(final List<Book> result) {
    if (cellListWithHeader != null) {
      dataProvider.setList(result);
      if (!dataProvider.getDataDisplays().contains(cellListWithHeader)) {
        dataProvider.addDataDisplay(cellListWithHeader);
      }
    }
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    if (dataProvider.getDataDisplays().contains(cellListWithHeader)) {
      dataProvider.removeDataDisplay(cellListWithHeader);
    }
  }

  @Override
  protected void start() {
    final MultiSelectionModel<Book> selectionModel = new MultiSelectionModel<Book>();
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        Set<Book> selectedSet = selectionModel.getSelectedSet();
        books = selectedSet;
      }
    });

    List<HasCell<Book, ?>> hasCells = new ArrayList<HasCell<Book, ?>>();

    hasCells.add(new HasCell<Book, Boolean>() {

      private CheckboxCell cell = new CheckboxCell(true, false);

      @Override
      public Cell<Boolean> getCell() {
        return cell;
      }

      @Override
      public FieldUpdater<Book, Boolean> getFieldUpdater() {
        return null;
      }

      @Override
      public Boolean getValue(final Book object) {
        return selectionModel.isSelected(object);
      }
    });

    hasCells.add(new HasCell<Book, Book>() {

      AbstractCell<Book> cell = new AbstractCell<Book>() {

        @Override
        public void render(final Context context, final Book value, final SafeHtmlBuilder sb) {
          sb.append(SafeHtmlUtils.fromTrustedString("<div style='display: inline-block;'>"
              + value.getTitle() + "  - " + value.getDescription() + "</div>"));
        }
      };

      @Override
      public Cell<Book> getCell() {
        return cell;
      }

      @Override
      public FieldUpdater<Book, Book> getFieldUpdater() {
        return null;
      }

      @Override
      public Book getValue(final Book object) {
        return object;
      }
    });

    CompositeCell<Book> compositeCell = new CompositeCell<Book>(hasCells) {

      @Override
      public void render(final Context context, final Book value, final SafeHtmlBuilder sb) {
        super.render(context, value, sb);
      }

      @Override
      protected com.google.gwt.dom.client.Element getContainerElement(
          final com.google.gwt.dom.client.Element parent) {
        return parent;
      }

      @Override
      protected <X> void render(final Context context, final Book value, final SafeHtmlBuilder sb,
          final HasCell<Book, X> hasCell) {
        Cell<X> cell = hasCell.getCell();
        cell.render(context, hasCell.getValue(value), sb);
      }
    };

    cellListWithHeader = new CellList<Book>(compositeCell);
    cellListWithHeader.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
    DefaultSelectionEventManager<Book> selectionEventMananger =
        DefaultSelectionEventManager.createCheckboxManager();
    cellListWithHeader.setSelectionModel(selectionModel, selectionEventMananger);
    dataProvider = new ListDataProvider<Book>();

    // cellListWithHeader.addCellSelectedHandler(new CellSelectedHandler() {
    //
    // @Override
    // public void onCellSelected(final CellSelectedEvent event) {
    // int index = event.getIndex();
    //
    // cellListWithHeader.setSelectedIndex(oldIndex, false);
    // cellListWithHeader.setSelectedIndex(index, true);
    // oldIndex = index;
    // }
    // });

    // cellListWithHeader.setRound(true);
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
