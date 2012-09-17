package com.goodow.web.reader.client;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.core.client.css.AppBundle;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.reader.client.ColumnSortEvent.ListHandler;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
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
import java.util.List;

public class BookList extends FlowView implements Receiver<List<Book>>, ColumnVisiableEvent.Handle {

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

  private DataGrid<Book> cellTable;

  private ListDataProvider<Book> dataProvider;

  private List<AbstractEditableCell<?, ?>> editableCells;

  private List<PendingChange<?>> pendingChanges = new ArrayList<BookList.PendingChange<?>>();

  private SortButtonCell.Delegate<String> delegate = new SortButtonCell.Delegate<String>() {

    @Override
    public void execute(final String object, final Column<?, ?> column, final Element curElm) {
      BookList.this.csddPanel.setCurColumn(column);
      popupContainer.show(curElm.<com.google.gwt.user.client.Element> cast(),
          BookList.this.csddPanel);
    }
  };

  ArrayList<Book> books;

  ListHandler<Book> listHandler;

  @Inject
  ButtonBar buttonBar;

  @Inject
  RefreshButton refreshButton;

  @Inject
  ActionButton actionButton;

  // @Inject
  // ActionButton dialogButton;

  @Inject
  TrashButton deleteButton;

  @Inject
  AsyncBookService bookService;

  @Inject
  ColumnSortDropDownPanel csddPanel;

  @Inject
  PopupContainer popupContainer;

  public <C> Column<Book, C> addColumn(final Cell<C> cell, final GetValue<C> getValue,
      final FieldUpdater<Book, C> fieldUpdater) {
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
    return column;
  }

  public Header<String> addHeader(final String name, final Column<?, ?> column) {
    Header<String> header = new Header<String>(new SortButtonCell<String>(delegate, column) {
      @Override
      public String getValue(final String value) {
        return value;
      }
    }) {

      @Override
      public String getValue() {
        return name;
      }
    };
    return header;
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
          books.clear();
        }
      });
    }

  }

  @Override
  public void onColumnVisiable(final ColumnVisiableEvent columnVisiableEvent) {
  }

  @Override
  public void onSuccess(final List<Book> result) {
    if (cellTable != null) {
      dataProvider.setList(result);
      listHandler.setList(dataProvider.getList());
      if (!dataProvider.getDataDisplays().contains(cellTable)) {
        dataProvider.addDataDisplay(cellTable);
      }
    }
  }

  public void removeHeaderElmShade() {
    Element headerElm1 = cellTable.getElement().getFirstChildElement().getFirstChildElement();
    Element headerElm2 = headerElm1.getNextSiblingElement();
    headerElm1.removeFromParent();
    headerElm2.removeFromParent();
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
    csddPanel.addColumnSortHandler(listHandler);
    csddPanel.addColumnVisiableHandle(this);

    ProvidesKey<Book> keyProvider = new ProvidesKey<Book>() {

      @Override
      public Object getKey(final Book item) {
        return item == null ? null : item.getId();
      }
    };
    cellTable = new DataGrid<Book>(6, keyProvider);
    cellTable.setWidth("100%");
    removeHeaderElmShade();

    final SelectionModel<Book> selectionModel = new MultiSelectionModel<Book>(keyProvider);
    cellTable.setSelectionModel(selectionModel, DefaultSelectionEventManager
        .<Book> createCheckboxManager(0));

    final Column<Book, Boolean> bookCheck = addColumn(new CheckboxCell(), new GetValue<Boolean>() {

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
        @SuppressWarnings("unused")
        boolean b =
            value ? !books.contains(object) ? books.add(object) : false : books.remove(object);
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
        for (Book book : visibleItems) {
          selectionModel.setSelected(book, value);
          bookCheck.getFieldUpdater().update(0, book, value);
        }
      }
    });
    cellTable.addColumn(bookCheck, header);
    cellTable.setColumnWidth(bookCheck, 2, Unit.PX);

    final String titleName = "书名";
    Column<Book, Book> bookTitleColumn = addColumn(cellProvider.get(), new GetValue<Book>() {

      @Override
      public Book getValue(final Book book) {
        return book;
      }
    }, null);
    Header<String> bookTitleHeader = addHeader(titleName, bookTitleColumn);
    listHandler.setComparator(bookTitleColumn, new Comparator<Book>() {
      @Override
      public int compare(final Book o1, final Book o2) {
        return o1.getTitle().compareTo(o2.getTitle());
      }
    });
    ColumnEntity<Book> bookTitleColumnEntity = new ColumnEntity<Book>(bookTitleColumn, 3, 2);
    putColumn(bookTitleHeader, bookTitleColumnEntity);

    final String selectionName = "是否推荐";
    Column<Book, Boolean> selectionColumn = addColumn(new CheckboxCell(), new GetValue<Boolean>() {

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
    ColumnEntity<Book> selectionColumnEntity = new ColumnEntity<Book>(selectionColumn, 2, 4);
    putColumn(addHeader(selectionName, selectionColumn), selectionColumnEntity);

    String descriptionName = "简介";
    final Column<Book, String> descriptionColumn =
        addColumn(new TextAreaCell(), new GetValue<String>() {

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
    ColumnEntity<Book> descriptionColumnEntity = new ColumnEntity<Book>(descriptionColumn, 3, 4);
    putColumn(addHeader(descriptionName, descriptionColumn), descriptionColumnEntity);

    String imageName = "图片";
    Column<Book, String> imageColumn = addColumn(new ImageCell(), new GetValue<String>() {

      @Override
      public String getValue(final Book book) {
        return "";
        // return GWT.getModuleBaseURL() + "resources/" + book.getCover().getId();
      }
    }, null);
    ColumnEntity<Book> imageColumnEntity = new ColumnEntity<Book>(imageColumn, 4, 4);
    putColumn(addHeader(imageName, imageColumn), imageColumnEntity);

    // FlowPanel flowPanel = new FlowPanel();
    // for (final Map.Entry<String, ColumnEntity<Book>> entry : columns.entrySet()) {
    // final Column<Book, ?> column = entry.getValue().getColumn();
    // String title = entry.getKey();
    // final CheckBox checkBox = new CheckBox(title);
    // flowPanel.add(checkBox);
    // cellTable.addColumn(column, title);
    // cellTable.setColumnWidth(column, entry.getValue().getWidth(), Unit.PX);
    // checkBox.setValue(true);
    // checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
    //
    // @Override
    // public void onValueChange(final ValueChangeEvent<Boolean> event) {
    // if (event.getValue()) {
    // cellTable.setColumnWidth(column, entry.getValue().getWidth(), Unit.PX);
    // } else {
    // cellTable.setColumnWidth(column, 0, Unit.PX);
    // }
    // }
    // });
    // }

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

  private void putColumn(final Header<String> header, final ColumnEntity<Book> entity) {
    Column<Book, ?> column = entity.getColumn();
    cellTable.addColumn(column, header);
    cellTable.setColumnWidth(column, entity.getWidth(), Unit.PX);
    csddPanel.addColumn(header.getValue(), entity);
  }
}
