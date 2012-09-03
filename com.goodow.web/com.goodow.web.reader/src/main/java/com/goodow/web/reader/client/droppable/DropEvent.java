package com.goodow.web.reader.client.droppable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventHandler;

public class DropEvent extends AbstractDraggableEvent<DropEvent.DropEventHandler> {

  public interface DropEventHandler extends EventHandler {
    public void onDrop(DropEvent event);
  }

  public static Type<DropEventHandler> TYPE = new Type<DropEventHandler>();

  public DropEvent(final Element draggable, final Element droppable) {
    super(draggable, droppable);
  }

  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<DropEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(final DropEventHandler handler) {
    handler.onDrop(this);
  }

}
