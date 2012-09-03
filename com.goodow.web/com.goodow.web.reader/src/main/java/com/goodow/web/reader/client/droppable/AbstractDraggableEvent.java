package com.goodow.web.reader.client.droppable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractDraggableEvent<H extends EventHandler> extends GwtEvent<H> {

  private DragAndDropContext context;

  public AbstractDraggableEvent(final Element draggable) {
    context = new DragAndDropContext(draggable);
  }

  public AbstractDraggableEvent(final Element draggable, final Element droppable) {
    context = new DragAndDropContext(draggable, droppable);
  }

  public Element getDraggable() {
    return context.getDraggable();
  }

  public <T> T getDraggableData() {
    return context.getDraggableData();
  }

  public Element getDroppable() {
    return context.getDroppable();
  }

  public <T> T getDroppableData() {
    return context.getDroppableData();
  }

  public Element getHelper() {
    return context.getHelper();
  }
}
