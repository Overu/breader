package com.goodow.web.reader.client;

import com.goodow.web.reader.client.ColumnSortEvent.ListHandler;
import com.goodow.web.reader.client.editgrid.Function;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

@Singleton
public class BookListView extends BaseViewBrowser {

  public interface Template extends SafeHtmlTemplates {
    @SafeHtmlTemplates.Template("<td><img style=\"height: 42px;width: 32px;\" src=\"{0}\"></td><td style=\"vertical-align: top;\"><div>{1}</div><div>{2}</div></td>")
    SafeHtml content(String imageUri, String title, String des);
  }

  class BookListCell extends AbstractCell<Book> {

    @Override
    public void render(final com.google.gwt.cell.client.Cell.Context context, final Book value,
        final SafeHtmlBuilder sb) {
      sb.append(template.content(value.getCover() == null ? "" : GWT.getModuleBaseURL()
          + "resources/" + value.getCover().getId(), value.getTitle(), value.getDescription()));
    }
  }

  private static Template template = GWT.create(Template.class);

  private CellList<Book> cellList;
  private FlowPanel main;
  private ListHandler<Book> listHandler;

  private InputElement allCheckElm;

  private boolean headerOnload = false;

  @Inject
  public BookListView() {
  }

  @Override
  public void commit() {
    return;
  }

  @Override
  public <T extends AbstractHasData<Book>> T getCellView() {
    return (T) cellList;
  }

  @Override
  public Widget getView() {
    return this;
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
    allCheckElm.setChecked(false);
  }

  @Override
  protected Widget init() {
    final BookListCell bookListCell = new BookListCell();
    main = new FlowPanel();
    listHandler = new ListHandler<Book>(dataProvider.getList());

    List<HasCell<Book, ?>> hasCells = new ArrayList<HasCell<Book, ?>>();
    hasCells.add(new HasCell<Book, Boolean>() {

      private CheckboxCell checkBoxCell = new CheckboxCell(true, false);

      @Override
      public Cell<Boolean> getCell() {
        return checkBoxCell;
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

      @Override
      public Cell<Book> getCell() {
        return bookListCell;
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

    CompositeCell<Book> cell = new CompositeCell<Book>(hasCells) {
      @Override
      public void render(final com.google.gwt.cell.client.Cell.Context context, final Book value,
          final SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<table><tbody><tr>");
        super.render(context, value, sb);
        sb.appendHtmlConstant("</tr></tbody></table>");
      }

      @Override
      protected Element getContainerElement(final Element parent) {
        return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
      }

      @Override
      protected <X> void render(final com.google.gwt.cell.client.Cell.Context context,
          final Book value, final SafeHtmlBuilder sb, final HasCell<Book, X> hasCell) {
        Cell<X> cell = hasCell.getCell();
        sb.appendHtmlConstant("<td style=\"padding: 0 0 0 7px;\">");
        cell.render(context, hasCell.getValue(value), sb);
        sb.appendHtmlConstant("</td>");
      }
    };

    cellList = new CellList<Book>(cell);
    cellList.setSelectionModel(selectionModel, DefaultSelectionEventManager
        .<Book> createCheckboxManager(0));
    main.add(cellList);
    return main;
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    if (headerOnload) {
      return;
    }
    headerOnload = true;
    addHeader();
  }

  private void addHeader() {
    if (columns.size() == 0) {
      headerOnload = false;
      return;
    }
    BookListViewHeader headerView = new BookListViewHeader();
    headerView.addColumnSortHandler(listHandler);
    headerView.addAllCheckHandle(new Function() {
      @Override
      public boolean f(final Event event) {
        if (allCheckElm == null) {
          Element as = Element.as(event.getEventTarget());
          if (!as.getTagName().equalsIgnoreCase("input")) {
            return true;
          }
          InputElement cast = as.<com.google.gwt.dom.client.InputElement> cast();
          allCheckElm = cast;
        }
        for (Book book : cellList.getVisibleItems()) {
          selectionModel.setSelected(book, allCheckElm.isChecked());
        }
        return true;
      }
    });

    for (Entry<String, ColumnEntity<?>> entry : columns.entrySet()) {
      String key = entry.getKey();
      ColumnEntity<Book> value = (ColumnEntity<Book>) entry.getValue();
      Comparator<Book> comparator = value.getComparator();
      if (comparator == null) {
        continue;
      }
      listHandler.setComparator(key, comparator);
      headerView.addSortLable(key);
    }
    main.insert(headerView, 0);
  }
}
