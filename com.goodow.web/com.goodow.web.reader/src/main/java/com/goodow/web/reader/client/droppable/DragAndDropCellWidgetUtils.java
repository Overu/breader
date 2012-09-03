package com.goodow.web.reader.client.droppable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;

public class DragAndDropCellWidgetUtils {

  public static final String VALUE_KEY = "__dragAndDropCellAssociatedValue";

  private static final DragAndDropCellWidgetUtils INSTANCE = new DragAndDropCellWidgetUtils();

  static DragAndDropCellWidgetUtils get() {
    return INSTANCE;
  }

  private DragAndDropCellWidgetUtils() {
  }

  void cleanCell(final Element cell) {
    if (cell == null) {
      return;
    }

    SimpleQuery qCell = SimpleQuery.q(cell);

    if (DraggableHandler.getInstance(cell) != null) {
      qCell.as(Draggable.Draggable).destroy();
    }

    // if (DroppableHandler.getInstance(cell) != null) {
    // qCell.as(Droppable).destroy();
    // }

    qCell.removeData(VALUE_KEY);
  }

  <C> void maybeMakeDraggableOrDroppable(final Element cell, final C value,
      final CellDragAndDropBehaviour cellDragAndDropBehaviour, final EventBus eventBus,
      final DraggableOptions darggableOptions) {

    SimpleQuery qCell = SimpleQuery.q(cell);

    if ((cellDragAndDropBehaviour == null || cellDragAndDropBehaviour.isDraggable())
        && DraggableHandler.getInstance(cell) == null) {
      qCell.as(Draggable.Draggable).draggable(eventBus, darggableOptions);
    }

    if ((cellDragAndDropBehaviour == null || cellDragAndDropBehaviour.isDroppable())
        && DraggableHandler.getInstance(cell) == null) {
      qCell.as(Draggable.Draggable).draggable(eventBus, darggableOptions);
    }

    qCell.data(VALUE_KEY, value);
  }
}
