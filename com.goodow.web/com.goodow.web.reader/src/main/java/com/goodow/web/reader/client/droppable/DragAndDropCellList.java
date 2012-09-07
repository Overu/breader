package com.goodow.web.reader.client.droppable;

import com.goodow.web.reader.client.droppable.DragStartEvent.DragStartEventHandler;
import com.goodow.web.reader.client.editgrid.Function;
import com.goodow.web.reader.client.style.ReadResources;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.ProvidesKey;

import java.util.List;

public class DragAndDropCellList<T> extends CellList<T> {

  private EventBus dragAndDropHandlerManager;

  private CellDragAndDropBehaviour cellDragAndDropBehaviour;

  private DraggableOptions draggableOptions;

  public DragAndDropCellList(final Cell<T> cell, final ProvidesKey<T> keyProvider) {
    this(cell, ReadResources.CELLLISTINSTANCE(), keyProvider, null);
  }

  public DragAndDropCellList(final Cell<T> cell, final Resources resources,
      final ProvidesKey<T> keyProvider, final CellDragAndDropBehaviour cellDragAndDropBehaviour) {
    super(cell, resources, keyProvider);
    this.setCellDragAndDropBehaviour(cellDragAndDropBehaviour);
    setDraggableOptions(new DraggableOptions());
  }

  public HandlerRegistration addDragStartHandler(final DragStartEventHandler handler) {
    return addDragAndDropHandler(handler, DragStartEvent.TYPE);
  }

  public CellDragAndDropBehaviour getCellDragAndDropBehaviour() {
    return cellDragAndDropBehaviour;
  }

  public DraggableOptions getDraggableOptions() {
    return draggableOptions;
  }

  public void setCellDragAndDropBehaviour(final CellDragAndDropBehaviour cellDragAndDropBehaviour) {
    this.cellDragAndDropBehaviour = cellDragAndDropBehaviour;
  }

  public void setCellDraggableOnly() {
    setCellDragAndDropBehaviour(new CellDragAndDropBehaviour.CellDragOnlyBehaviour());

  }

  public void setCellDroppableOnly() {
    setCellDragAndDropBehaviour(new CellDragAndDropBehaviour.CellDropOnlyBehaviour());
  }

  public void setDraggableOptions(final DraggableOptions draggableOptions) {
    this.draggableOptions = draggableOptions;
  }

  public void setScope(final String scope) {
    draggableOptions.setScope(scope);
  }

  protected void addDragAndDropBehaviour(final List<T> values, final int start) {
    int end = start + values.size();

    for (int rowIndex = start; rowIndex < end; rowIndex++) {
      T value = values.get(rowIndex - start);
      Element newCell = getRowElement(rowIndex);

      DragAndDropCellWidgetUtils.get().maybeMakeDraggableOrDroppable(newCell, value,
          cellDragAndDropBehaviour, ensureDrangAndDropHandlers(), getDraggableOptions());
    }

  }

  protected final <H extends EventHandler> HandlerRegistration addDragAndDropHandler(
      final H handler, final Type<H> type) {
    return ensureDrangAndDropHandlers().addHandler(type, handler);
  }

  protected void cleanAllCells() {
    SimpleQuery.q(getChildContainer()).children(new Function() {
      @Override
      public boolean f(final Element element) {
        DragAndDropCellWidgetUtils.get().cleanCell(element);
        return false;
      }
    });
  }

  protected EventBus ensureDrangAndDropHandlers() {
    return dragAndDropHandlerManager == null ? dragAndDropHandlerManager = new SimpleEventBus()
        : dragAndDropHandlerManager;
  }

  @Override
  protected void onUnload() {
    super.onUnload();
  }

  @Override
  protected void replaceAllChildren(final List<T> values, final SafeHtml html) {
    cleanAllCells();

    super.replaceAllChildren(values, html);

    addDragAndDropBehaviour(values, 0);
  }

  @Override
  protected void replaceChildren(final List<T> values, final int start, final SafeHtml html) {
    int end = start + values.size();
    for (int rowIndex = start; rowIndex < end; rowIndex++) {
      Element oldCell = getRowElement(rowIndex);
      DragAndDropCellWidgetUtils.get().cleanCell(oldCell);
    }

    super.replaceChildren(values, start, html);

    addDragAndDropBehaviour(values, start);
  }
}
