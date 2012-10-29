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
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
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

  // private static class BookTitleChange extends PendingChange<String> {
  //
  // public BookTitleChange(final Book book, final String value) {
  // super(book, value);
  // }
  //
  // @Override
  // protected void doCommit(final Book book, final String value) {
  // book.setTitle(value);
  // }
  // }

  private static class CheckBoxCell extends AbstractEditableCell<Boolean, Boolean> {

    private static final SafeHtml INPUT_CHECKED = SafeHtmlUtils
        .fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" checked/>");

    private static final SafeHtml INPUT_UNCHECKED = SafeHtmlUtils
        .fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\"/>");

    private boolean isClearChecked = false;

    public CheckBoxCell() {
      super(BrowserEvents.CHANGE, BrowserEvents.KEYDOWN);
    }

    public boolean isClearChecked() {
      return isClearChecked;
    }

    @Override
    public boolean isEditing(final com.google.gwt.cell.client.Cell.Context context,
        final Element parent, final Boolean value) {
      return false;
    }

    @Override
    public void onBrowserEvent(final com.google.gwt.cell.client.Cell.Context context,
        final Element parent, final Boolean value, final NativeEvent event,
        final ValueUpdater<Boolean> valueUpdater) {
      String type = event.getType();
      boolean enterPressed =
          BrowserEvents.KEYDOWN.equals(type) && event.getKeyCode() == KeyCodes.KEY_ENTER;
      if (BrowserEvents.CHANGE.equals(type) || enterPressed) {
        InputElement input = parent.getFirstChild().cast();
        Boolean isChecked = input.isChecked();

        if (enterPressed && (handlesSelection() || !dependsOnSelection())) {
          isChecked = !isChecked;
          input.setChecked(isChecked);
        }

        if (value != isChecked && !dependsOnSelection()) {
          setViewData(context.getKey(), isChecked);
        } else {
          clearViewData(context.getKey());
        }

        if (valueUpdater != null) {
          valueUpdater.update(isChecked);
        }
      }
    }

    @Override
    public void render(final com.google.gwt.cell.client.Cell.Context context, final Boolean value,
        final SafeHtmlBuilder sb) {
      Object key = context.getKey();
      if (isClearChecked()) {
        setClearChecked(false);
        sb.append(INPUT_UNCHECKED);
        clearViewData(key);
        return;
      }

      Boolean viewData = getViewData(key);
      if (viewData != null && viewData.equals(value)) {
        clearViewData(key);
        viewData = null;
      }

      if (value != null && ((viewData != null) ? viewData : value)) {
        sb.append(INPUT_CHECKED);
      } else {
        sb.append(INPUT_UNCHECKED);
      }
    }

    public void setClearChecked(final boolean isClearChecked) {
      this.isClearChecked = isClearChecked;
    }
  }

  private static interface GetValue<C> {
    C getValue(Book book);
  }

  private abstract static class PendingChange<T> {

    public static AsyncBookService getBookServic() {
      return bookServic;
    }

    public static void setBookServic(final AsyncBookService bookServic) {
      PendingChange.bookServic = bookServic;
    }

    private final Book book;

    private final T value;

    private static AsyncBookService bookServic;

    public PendingChange(final Book book, final T value) {
      this.book = book;
      this.value = value;
    }

    public void commit() {
      doCommit(book, value);
      getBookServic().save(book).fire(new Receiver<Book>() {

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

  private CheckBoxCell allCheckcell;

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
    allCheckcell = new CheckBoxCell();
    Header<Boolean> checkHeader = new Header<Boolean>(allCheckcell) {

      @Override
      public Boolean getValue() {
        return false;
      }
    };
    checkHeader.setUpdater(new ValueUpdater<Boolean>() {

      @Override
      public void update(final Boolean value) {
        List<Book> visibleItems = getCellTable().getVisibleItems();
        for (Book book : visibleItems) {
          selectionModel.setSelected(book, value);
          bookCheck.getFieldUpdater().update(0, book, value);
        }
      }
    });
    getCellTable().addColumn(bookCheck, checkHeader);
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
    Header<String> header = new Header<String>(new TextCell()) {

      @Override
      public String getValue() {
        return name;
      }
    };
    return header;
  }

  @Override
  public void commit() {
    if (PendingChange.getBookServic() == null) {
      PendingChange.setBookServic(bookService);
    }

    for (PendingChange<?> pendingChange : pendingChanges) {
      pendingChange.commit();
    }
    pendingChanges.clear();

    dataProvider.refresh();
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
  protected void clearViewChected(final boolean ignore) {
    if (ignore) {
      return;
    }
    allCheckcell.setClearChecked(true);
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
