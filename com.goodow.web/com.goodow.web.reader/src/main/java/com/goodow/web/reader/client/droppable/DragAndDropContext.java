package com.goodow.web.reader.client.droppable;

import com.google.gwt.dom.client.Element;

public class DragAndDropContext {

  public static final String VALUE_KEY = "__dragAndDropCellAssociatedValue";

  private Element draggable;
  private Element droppable;
  private Element helper;

  public DragAndDropContext(final Element draggable) {
    this(draggable, null);
  }

  public DragAndDropContext(final Element draggable, final Element droppable) {
    this.draggable = draggable;
    this.droppable = droppable;
    init();
  }

  public Element getDraggable() {
    assert draggable != null : "draggable cannot be null";
    return draggable;
  }

  @SuppressWarnings("unchecked")
  public <T> T getDraggableData() {
    assert draggable != null : "draggable data cannot be null";
    return (T) SimpleQuery.q(draggable).data(DragAndDropContext.VALUE_KEY);
  }

  public Element getDroppable() {
    assert droppable != null : "droppable cannot be null";
    return droppable;
  }

  @SuppressWarnings("unchecked")
  public <T> T getDroppableData() {
    assert droppable != null : "droppable data cannot be null";
    return (T) SimpleQuery.q(droppable).data(DragAndDropContext.VALUE_KEY);
  }

  public Element getHelper() {
    assert helper != null : "helper cannot be null";
    return helper;
  }

  private void init() {
    DraggableHandler handler = DraggableHandler.getInstance(draggable);
    if (handler.getHelper() != null) {
      helper = handler.getHelper();
    }
  }
}
