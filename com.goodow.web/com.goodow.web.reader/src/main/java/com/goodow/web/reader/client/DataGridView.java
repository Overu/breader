package com.goodow.web.reader.client;

import com.goodow.web.core.shared.Receiver;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Singleton
public class DataGridView extends BaseViewBrowser {

  private static class BookSelectionChange extends PendingChange<Boolean> {

    public BookSelectionChange(final Book book, final Boolean value) {
      super(book, value);
    }

    @Override
    protected void doCommit(final Book book, final Boolean value) {
      book.setSelected(value);
    }
  }

  private static class BookTextAreaChange extends PendingChange<String> {

    public BookTextAreaChange(final Book book, final String value) {
      super(book, value);
    }

    @Override
    protected void doCommit(final Book book, final String value) {
      book.setDescription(value);
    }
  }

  private static class BookTitleChange extends PendingChange<String> {

    public BookTitleChange(final Book book, final String value) {
      super(book, value);
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
    @Inject
    AsyncBookService bookServic2;

    public PendingChange(final Book book, final T value) {
      this.book = book;
      this.value = value;
    }

    public void commit() {
      doCommit(book, value);
      bookServic2.save(book).fire(new Receiver<Book>() {

        @Override
        public void onSuccess(final Book result) {
        }
      });
    }

    protected abstract void doCommit(Book book, T value);
  }

  Provider<BookHyperlinkCell> cellProvider;

  private DataGrid<Book> dataGrid;

  private ListHandler<Book> listHandler;

  private List<AbstractEditableCell<?, ?>> editableCells;
  private List<PendingChange<?>> pendingChanges = new ArrayList<DataGridView.PendingChange<?>>();
  private List<Book> books;

  @Inject
  public DataGridView(final Provider<BookHyperlinkCell> cellProvider) {
    this.cellProvider = cellProvider;

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
        List<Book> visibleItems = getCellTable().getVisibleItems();
        for (Book book : visibleItems) {
          selectionModel.setSelected(book, value);
          bookCheck.getFieldUpdater().update(0, book, value);
        }
      }
    });
    getCellTable().addColumn(bookCheck, header);
    getCellTable().setColumnWidth(bookCheck, 2, Unit.PX);

    final String titleName = "书名";
    Column<Book, Book> bookTitleColumn = addColumn(cellProvider.get(), new GetValue<Book>() {

      @Override
      public Book getValue(final Book book) {
        return book;
      }
    }, null);
    Header<String> bookTitleHeader = addHeader(titleName);
    putColumn(bookTitleHeader, bookTitleColumn, 2, new Comparator<Book>() {
      @Override
      public int compare(final Book o1, final Book o2) {
        return o1.getTitle().compareTo(o2.getTitle());
      }
    });

    final String selectionName = "是否推荐";
    Column<Book, Boolean> selectionColumn = addColumn(new CheckboxCell(), new GetValue<Boolean>() {

      @Override
      public Boolean getValue(final Book book) {
        return book.isSelected();
      }
    }, new FieldUpdater<Book, Boolean>() {

      @Override
      public void update(final int index, final Book object, final Boolean value) {
        pendingChanges.add(new BookSelectionChange(object, value));
      }
    });
    putColumn(addHeader(selectionName), selectionColumn, 4);

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
            pendingChanges.add(new BookTextAreaChange(object, value));
          }
        });
    putColumn(addHeader(descriptionName), descriptionColumn, 4);

    String imageName = "图片";
    Column<Book, String> imageColumn = addColumn(new ImageCell(), new GetValue<String>() {

      @Override
      public String getValue(final Book book) {
        return "";
        // return GWT.getModuleBaseURL() + "resources/" + book.getCover().getId();
      }
    }, null);
    putColumn(addHeader(imageName), imageColumn, 4);

    // refreshButton.addTapHandler(new TapHandler() {
    //
    // @Override
    // public void onTap(final TapEvent event) {
    // refresh();
    // }
    // });
    //
    // deleteButton.addTapHandler(new TapHandler() {
    //
    // @Override
    // public void onTap(final TapEvent event) {
    // delete();
    // }
    // });
    //
    // actionButton.addTapHandler(new TapHandler() {
    //
    // @Override
    // public void onTap(final TapEvent event) {
    // for (PendingChange<?> pendingChange : pendingChanges) {
    // pendingChange.commit();
    // }
    // pendingChanges.clear();
    //
    // dataProvider.refresh();
    // }
    // });
  }

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

  public Header<String> addHeader(final String name) {
    // Header<String> header = new Header<String>(new SortButtonCell<String>(delegate, column) {
    Header<String> header = new Header<String>(new TextCell()) {

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

  public DataGrid<Book> getCellTable() {
    return dataGrid;
  }

  @Override
  public <T extends AbstractHasData<Book>> T getCellView() {
    return (T) dataGrid;
  }

  @Override
  public Widget getView() {
    return this;
  }

  @Override
  public void refresh() {
    bookService.getMyBooks().fire(this);
  }

  public void removeHeaderElmShade() {
    Element headerElm1 = getCellTable().getElement().getFirstChildElement().getFirstChildElement();
    Element headerElm2 = headerElm1.getNextSiblingElement();
    headerElm1.removeFromParent();
    headerElm2.removeFromParent();
  }

  @Override
  public void setListHandler() {
    listHandler.setList(dataProvider.getList());
  }

  @Override
  protected Widget init() {
    dataGrid = new DataGrid<Book>(6, keyProvider);
    getCellTable().setWidth("100%");
    removeHeaderElmShade();

    editableCells = new ArrayList<AbstractEditableCell<?, ?>>();

    listHandler = new ListHandler<Book>(dataProvider.getList());
    dataGrid.addColumnSortHandler(listHandler);

    getCellTable().setSelectionModel(selectionModel,
        DefaultSelectionEventManager.<Book> createCheckboxManager(0));
    return dataGrid;
  }

  private void putColumn(final Header<String> header, final Column<Book, ?> column, final int width) {
    putColumn(header, column, width, null);
  }

  private void putColumn(final Header<String> header, final Column<Book, ?> column,
      final int width, final Comparator<Book> comparator) {
    getCellTable().addColumn(column, header);
    getCellTable().setColumnWidth(column, width, Unit.PX);
    if (comparator != null) {
      column.setSortable(true);
      listHandler.setComparator(column, comparator);
    }
    ColumnEntity<Book> entity = new ColumnEntity<Book>(column, width, comparator);
    columns.put(header.getValue(), entity);
  }
}
