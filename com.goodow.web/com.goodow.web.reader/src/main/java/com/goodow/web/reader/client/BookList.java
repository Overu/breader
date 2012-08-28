package com.goodow.web.reader.client;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.core.client.css.AppBundle;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ActionButton;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.googlecode.mgwt.ui.client.widget.buttonbar.RefreshButton;
import com.googlecode.mgwt.ui.client.widget.buttonbar.TrashButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookList extends FlowView implements Receiver<List<Book>> {

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

  private static class BookTextAreaChange extends PendingChange<String> {

    public BookTextAreaChange(final Book book, final String value,
        final AsyncBookService bookService) {
      super(book, value, bookService);
    }

    @Override
    protected void doCommit(final Book book, final String value) {
      book.setDescription(value);
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

  @Inject
  Provider<BookHyperlinkCell> cellProvider;

  // @Inject
  // private ScrollPanel scrollPanel;

  private static Map<String, ColumnEntity<Book>> columns;

  private DataGrid<Book> cellTable;

  private ListDataProvider<Book> dataProvider;

  private List<AbstractEditableCell<?, ?>> editableCells;

  private List<PendingChange<?>> pendingChanges = new ArrayList<BookList.PendingChange<?>>();

  ArrayList<Book> books;

  ListHandler<Book> listHandler;

  @Inject
  ButtonBar buttonBar;

  @Inject
  RefreshButton refreshButton;

  @Inject
  ActionButton actionButton;

  @Inject
  ActionButton dialogButton;

  @Inject
  TrashButton deleteButton;

  @Inject
  AsyncBookService bookService;

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

    // cellTable.addColumn(column, headerString);
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
      listHandler.setList(dataProvider.getList());
      cellTable.addColumnSortHandler(listHandler);
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
    listHandler = new ListHandler<Book>(dataProvider.getList());
    columns = new HashMap<String, ColumnEntity<Book>>();
    final PopupPanel pp = new PopupPanel(true);
    pp.setAutoHideOnHistoryEventsEnabled(true);
    pp.setAnimationEnabled(true);

    ProvidesKey<Book> keyProvider = new ProvidesKey<Book>() {

      @Override
      public Object getKey(final Book item) {
        return item == null ? null : item.getId();
      }
    };
    cellTable = new DataGrid<Book>(6, keyProvider);
    cellTable.setWidth("100%");

    final SelectionModel<Book> selectionModel = new MultiSelectionModel<Book>(keyProvider);
    cellTable.setSelectionModel(selectionModel, DefaultSelectionEventManager
        .<Book> createCheckboxManager(0));

    final Column<Book, Boolean> bookCheck =
        addColumn(new CheckboxCell(), "选择", new GetValue<Boolean>() {

          @Override
          public Boolean getValue(final Book book) {
            return selectionModel.isSelected(book);
          }
        }, new FieldUpdater<Book, Boolean>() {

          @Override
          public void update(final int index, final Book object, final Boolean value) {
            if (books == null) {
              books = new ArrayList<Book>();
            }
            boolean b = value ? books.add(object) : books.remove(object);
            selectionModel.setSelected(object, value);
          }
        });
    Header<Boolean> header = new Header<Boolean>(new CheckboxCell()) {

      @Override
      public Boolean getValue() {
        return false;
      }
    };
    header.setUpdater(new ValueUpdater<Boolean>() {

      @Override
      public void update(final Boolean value) {
        List<Book> visibleItems = cellTable.getVisibleItems();
        for (int i = 0; i < visibleItems.size(); i++) {
          Book object = visibleItems.get(i);
          bookCheck.getFieldUpdater().update(i, object, value);
        }
      }
    });
    cellTable.addColumn(bookCheck, header);
    cellTable.setColumnWidth(bookCheck, 2, Unit.PX);

    // Column<Book, String> bookTitle = addColumn(new EditTextCell(), "书名", new GetValue<String>() {
    //
    // @Override
    // public String getValue(final Book book) {
    // return book.getTitle();
    // }
    // }, new FieldUpdater<Book, String>() {
    //
    // @Override
    // public void update(final int index, final Book object, final String value) {
    // pendingChanges.add(new BookTitleChange(object, value, bookService));
    // }
    // });
    Column<Book, Book> bookTitle = addColumn(cellProvider.get(), "书名", new GetValue<Book>() {

      @Override
      public Book getValue(final Book book) {
        return book;
      }
    }, null);
    bookTitle.setSortable(true);
    listHandler.setComparator(bookTitle, new Comparator<Book>() {

      @Override
      public int compare(final Book o1, final Book o2) {
        return o1.getTitle().compareTo(o2.getTitle());
      }
    });
    ColumnEntity<Book> bookTitleColumn = new ColumnEntity<Book>(bookTitle, 1, 4);
    columns.put("书名", bookTitleColumn);

    Column<Book, Boolean> bookSelection =
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
    ColumnEntity<Book> bookSelectionColumn = new ColumnEntity<Book>(bookSelection, 2, 4);
    columns.put("是否推荐", bookSelectionColumn);

    final Column<Book, String> bookDescription =
        addColumn(new TextAreaCell(), "简介", new GetValue<String>() {

          @Override
          public String getValue(final Book book) {
            return book.getDescription();
          }
        }, new FieldUpdater<Book, String>() {

          @Override
          public void update(final int index, final Book object, final String value) {
            pendingChanges.add(new BookTextAreaChange(object, value, bookService));
          }
        });
    ColumnEntity<Book> bookDescriptionColumn = new ColumnEntity<Book>(bookDescription, 3, 4);
    columns.put("简介", bookDescriptionColumn);

    Column<Book, String> bookImage = addColumn(new ImageCell(), "图片", new GetValue<String>() {

      @Override
      public String getValue(final Book book) {
        return GWT.getModuleBaseURL() + "resources/" + book.getCover().getId();
      }
    }, null);
    ColumnEntity<Book> bookImageColumn = new ColumnEntity<Book>(bookImage, 4, 4);
    columns.put("图片", bookImageColumn);

    FlowPanel flowPanel = new FlowPanel();
    for (final Map.Entry<String, ColumnEntity<Book>> entry : columns.entrySet()) {
      final Column<Book, ?> column = entry.getValue().getColumn();
      String title = entry.getKey();
      final CheckBox checkBox = new CheckBox(title);
      flowPanel.add(checkBox);
      cellTable.addColumn(column, title);
      cellTable.setColumnWidth(column, entry.getValue().getWidth(), Unit.PX);
      checkBox.setValue(true);
      checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

        @Override
        public void onValueChange(final ValueChangeEvent<Boolean> event) {
          if (event.getValue()) {
            cellTable.setColumnWidth(column, entry.getValue().getWidth(), Unit.PX);
          } else {
            cellTable.setColumnWidth(column, 0, Unit.PX);
          }
        }
      });
    }
    pp.add(flowPanel);

    // scrollPanel.setWidget(new SimplePanel(cellTable));
    // scrollPanel.setScrollingEnabledX(false);
    // scrollPanel.addStyleName(AppBundle.INSTANCE.css().webKitFlex());

    cellTable.addStyleName(AppBundle.INSTANCE.css().webKitFlex());

    buttonBar.add(refreshButton);
    buttonBar.add(deleteButton);
    buttonBar.add(actionButton);
    buttonBar.add(dialogButton);

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

    dialogButton.addTapHandler(new TapHandler() {

      @Override
      public void onTap(final TapEvent event) {
        Widget widget = (Widget) event.getSource();
        int absoluteLeft = widget.getAbsoluteLeft() - 10;
        int absoluteTop = widget.getAbsoluteTop() - 20;
        pp.setPopupPosition(absoluteLeft, absoluteTop);
        pp.show();
      }
    });

    refresh();
  }
}
