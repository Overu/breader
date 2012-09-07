package com.goodow.web.reader.client.droppable;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.reader.client.droppable.DragStartEvent.DragStartEventHandler;
import com.goodow.web.reader.client.droppable.DraggableOptions.HelperType;
import com.goodow.web.reader.client.editgrid.Function;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;

import java.util.List;

public class CellListDrag extends FlowView implements DragStartEventHandler {

  private ListDataProvider<Book> dataProvider;

  private DragAndDropCellList<Book> cellList;

  private Function f;

  @Inject
  AsyncBookService bookService;

  private AbstractCell<Book> cell = new AbstractCell<Book>() {

    @Override
    public void render(final Context context, final Book value, final SafeHtmlBuilder sb) {
      sb.appendHtmlConstant("<div>" + value.getTitle() + "</div>");
    }
  };

  @Override
  public void onDragStart(final DragStartEvent event) {
    if (f == null) {
      return;
    }
    f.f(event);
  }

  @Override
  public void onLoad() {
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
  }

  public void setFunction(final Function f) {
    this.f = f;
  }

  public void setScope(final String scope) {
    cellList.setScope(scope);
  }

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
    cellList.getDraggableOptions().setHelperType(HelperType.CLONE);

    cellList.addDragStartHandler(this);

    // final TextArea textArea = new TextArea();

    // DroppableWidget<TextArea> dw = new DroppableWidget<TextArea>(textArea);
    // dw.addDropHandler(new DropEvent.DropEventHandler() {
    //
    // @Override
    // public void onDrop(final DropEvent event) {
    // Book draggableData = (Book) event.getDraggableData();
    // textArea.setValue(draggableData.getTitle());
    // }
    //
    // }, DropEvent.TYPE);
    //
    // final TextArea textArea1 = new TextArea();
    //
    // DroppableWidget<TextArea> dw1 = new DroppableWidget<TextArea>(textArea1);
    // dw1.addDropHandler(new DropEvent.DropEventHandler() {
    //
    // @Override
    // public void onDrop(final DropEvent event) {
    // Book draggableData = (Book) event.getDraggableData();
    // textArea1.setValue(draggableData.getTitle());
    // }
    //
    // }, DropEvent.TYPE);

    main.add(cellList);
    // main.add(dw);
    // main.add(dw1);
  }
}
