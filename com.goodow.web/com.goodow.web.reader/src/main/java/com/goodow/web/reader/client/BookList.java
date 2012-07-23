package com.goodow.web.reader.client;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.core.client.css.AppBundle;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.reader.client.style.ReadResources.CellListResources;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.inject.Inject;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ActionButton;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.googlecode.mgwt.ui.client.widget.buttonbar.RefreshButton;
import com.googlecode.mgwt.ui.client.widget.buttonbar.TrashButton;

import java.util.ArrayList;
import java.util.List;

public class BookList extends FlowView implements Receiver<List<Book>> {

  public interface Template extends SafeHtmlTemplates {
    @SafeHtmlTemplates.Template("<div class=\"{0}\"><table><tr><td><img width='50px' height='80px' src='{1}'/></td><td valign=\"top\"><div>{2}</div><div>{3}</div><div>{4}</div></td></tr></table></div>")
    SafeHtml content(String classCss, String url, String title, String author, String description);
  }

  private static class BookSelectionChange extends PendingChange<Boolean> {

    public BookSelectionChange(final Book book, final Boolean value,
        final AsyncBookService bookService) {
      super(book, value, bookService);
    }

    @Override
    protected void doCommit(final Book book, final Boolean value) {
      book.setSelected(value);
    }
  }

  private static class BookTitleChange extends PendingChange<String> {

    public BookTitleChange(final Book book, final String value, final AsyncBookService bookService) {
      super(book, value, bookService);
    }

    @Override
    protected void doCommit(final Book book, final String value) {
      book.setTitle(value);
    }
  }

  private static interface GetValue<C> {
    C getValue(Book book);
  }

  private abstract static class PendingChange<T> {
    private final Book book;
    private final T value;
    private final AsyncBookService bookService2;

    public PendingChange(final Book book, final T value, final AsyncBookService bookService) {
      this.book = book;
      this.value = value;
      bookService2 = bookService;
    }

    public void commit() {
      doCommit(book, value);
      bookService2.save(book).fire(new Receiver<Book>() {

        @Override
        public void onSuccess(final Book result) {
        }
      });
    }

    protected abstract void doCommit(Book book, T value);
  }

  // @Inject
  // private ScrollPanel scrollPanel;

  private DataGrid<Book> cellTable;

  private ListDataProvider<Book> dataProvider;

  private List<AbstractEditableCell<?, ?>> editableCells;

  private List<PendingChange<?>> pendingChanges = new ArrayList<BookList.PendingChange<?>>();

  ArrayList<Book> books;

  @Inject
  ButtonBar buttonBar;

  @Inject
  RefreshButton refreshButton;

  @Inject
  ActionButton actionButton;

  @Inject
  TrashButton deleteButton;

  @Inject
  AsyncBookService bookService;

  @Inject
  CellListResources cellListResources;

  // private static Template TEMPLATE = GWT.create(Template.class);

  public <C> Column<Book, C> addColumn(final Cell<C> cell, final String headerString,
      final GetValue<C> getValue, final FieldUpdater<Book, C> fieldUpdater) {
    Column<Book, C> column = new Column<Book, C>(cell) {

      @Override
      public C getValue(final Book object) {
        return getValue.getValue(object);
      }
    };

    column.setFieldUpdater(fieldUpdater);

    if (cell instanceof AbstractEditableCell<?, ?>) {
      editableCells.add((AbstractEditableCell<?, ?>) cell);
    }

    cellTable.addColumn(column, headerString);
    return column;
  }

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
    if (cellTable != null) {
      dataProvider.setList(result);
      if (!dataProvider.getDataDisplays().contains(cellTable)) {
        dataProvider.addDataDisplay(cellTable);
      }
    }
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    if (dataProvider.getDataDisplays().contains(cellTable)) {
      dataProvider.removeDataDisplay(cellTable);
    }
  }

  @Override
  protected void start() {
    editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
    dataProvider = new ListDataProvider<Book>();

    cellTable = new DataGrid<Book>(6, new ProvidesKey<Book>() {

      @Override
      public Object getKey(final Book item) {
        return item == null ? null : item.getId();
      }
    });
    // cellTable.setMinimumTableWidth(140, Unit.EM);

    addColumn(new CheckboxCell(), "选择", new GetValue<Boolean>() {

      @Override
      public Boolean getValue(final Book book) {
        return false;
      }
    }, new FieldUpdater<Book, Boolean>() {

      @Override
      public void update(final int index, final Book object, final Boolean value) {
        if (books == null) {
          books = new ArrayList<Book>();
        }
        boolean b = value ? books.add(object) : books.remove(object);
      }
    });

    Column<Book, String> bookTitle = addColumn(new EditTextCell(), "书名", new GetValue<String>() {

      @Override
      public String getValue(final Book book) {
        return book.getTitle();
      }
    }, new FieldUpdater<Book, String>() {

      @Override
      public void update(final int index, final Book object, final String value) {
        pendingChanges.add(new BookTitleChange(object, value, bookService));
      }
    });
    cellTable.setColumnWidth(bookTitle, 16, Unit.EM);

    addColumn(new CheckboxCell(), "是否推荐", new GetValue<Boolean>() {

      @Override
      public Boolean getValue(final Book book) {
        return book.isSelected();
      }
    }, new FieldUpdater<Book, Boolean>() {

      @Override
      public void update(final int index, final Book object, final Boolean value) {
        pendingChanges.add(new BookSelectionChange(object, value, bookService));
      }
    });

    // scrollPanel.setWidget(new SimplePanel(cellTable));
    // scrollPanel.setScrollingEnabledX(false);
    // scrollPanel.addStyleName(AppBundle.INSTANCE.css().webKitFlex());
    cellTable.addStyleName(AppBundle.INSTANCE.css().webKitFlex());

    buttonBar.add(refreshButton);
    buttonBar.add(deleteButton);
    buttonBar.add(actionButton);

    main.addStyleName(AppBundle.INSTANCE.css().fullScreenStyle());

    main.add(cellTable);

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

    actionButton.addTapHandler(new TapHandler() {

      @Override
      public void onTap(final TapEvent event) {
        for (PendingChange<?> pendingChange : pendingChanges) {
          pendingChange.commit();
        }
        pendingChanges.clear();

        dataProvider.refresh();
      }
    });

    refresh();
  }
}
