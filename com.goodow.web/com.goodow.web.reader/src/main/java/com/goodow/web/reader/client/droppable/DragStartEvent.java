package com.goodow.web.reader.client.droppable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventHandler;

public class DragStartEvent extends AbstractDraggableEvent<DragStartEvent.DragStartEventHandler> {

  public interface DragStartEventHandler extends EventHandler {
    public void onDragStart(DragStartEvent event);
  }

  public static Type<DragStartEventHandler> TYPE = new Type<DragStartEventHandler>();

  public DragStartEvent(final Element draggable) {
    super(draggable);
  }

  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<DragStartEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(final DragStartEventHandler handler) {
    handler.onDragStart(this);
  }

}
