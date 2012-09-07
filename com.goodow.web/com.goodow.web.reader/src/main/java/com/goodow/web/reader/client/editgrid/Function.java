package com.goodow.web.reader.client.editgrid;

import com.goodow.web.reader.client.droppable.AbstractDraggableEvent;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;

public class Function {

  public boolean f(final AbstractDraggableEvent event) {
    return true;
  }

  public boolean f(final EditGridCell cell, final Event event) {
    return true;
  }

  public boolean f(final Element element) {
    return true;
  }

  public boolean f(final Event event) {
    return true;
  }

  public boolean f(final Event event, final Object d) {
    return f(event);
  }

}
