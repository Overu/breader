package com.goodow.web.reader.client.droppable;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.reader.client.droppable.DraggableOptions.HelperType;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;

import java.util.List;

public class CellListDrag extends FlowView {

  private ListDataProvider<Book> dataProvider;

  private DragAndDropCellList<Book> cellList;

  @Inject
  AsyncBookService bookService;

  private AbstractCell<Book> cell = new AbstractCell<Book>() {

    @Override
    public void render(final Context context, final Book value, final SafeHtmlBuilder sb) {
      sb.appendHtmlConstant("<div>" + value.getTitle() + "</div>");
    }
  };

  @Override
  public void refresh() {
    bookService.getMyBooks().fire(new Receiver<List<Book>>() {

      @Override
      public void onSuccess(final List<Book> result) {
        if (cellList != null) {
          dataProvider.setList(result);
          if (!dataProvider.getDataDisplays().contains(cellList)) {
            dataProvider.addDataDisplay(cellList);
          }
        }
      }
    });
  };

  @Override
  protected void onUnload() {
    super.onUnload();
    if (dataProvider.getDataDisplays().contains(cellList)) {
      dataProvider.removeDataDisplay(cellList);
    }
  }

  @Override
  protected void start() {
    dataProvider = new ListDataProvider<Book>();
    cellList = new DragAndDropCellList<Book>(cell, null);
    Element createDiv = DOM.createDiv();
    createDiv.setInnerText("asfd");
    cellList.getDraggableOptions().setHelperType(HelperType.CLONE);
    main.add(cellList);
  }
}
