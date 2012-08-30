package com.goodow.web.reader.client.droppable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class DragStartEvent extends GwtEvent<DragStartEvent.DragStartEventHandler> {

  public interface DragStartEventHandler extends EventHandler {
    public void onDragStart(DragStartEvent event);
  }

  public static Type<DragStartEventHandler> TYPE = new Type<DragStartEventHandler>();

  public DragStartEvent(final Element draggable) {
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
