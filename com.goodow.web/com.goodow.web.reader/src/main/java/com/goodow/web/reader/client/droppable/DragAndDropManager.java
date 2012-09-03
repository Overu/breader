package com.goodow.web.reader.client.droppable;

import static com.google.gwt.user.client.Event.ONMOUSEDOWN;

import com.goodow.web.reader.client.droppable.DroppableOptions.AcceptFunction;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DragAndDropManager {

  public static DragAndDropManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DragAndDropManager();
    }
    return INSTANCE;
  }

  private Element currentDraggable;
  private Map<String, Collection<Element>> droppablesByScope;

  private static DragAndDropManager INSTANCE;
  private static boolean droppableOnly = false;

  private DragAndDropManager() {
    droppablesByScope = new HashMap<String, Collection<Element>>();
    droppablesByScope.put("default", new ArrayList<Element>());
  }

  public void addDroppable(final Element droppable, final String scope) {
    Collection<Element> droppables = droppablesByScope.get(scope);
    if (droppables == null) {
      droppables = new ArrayList<Element>();
      droppablesByScope.put(scope, droppables);
    }
    droppables.add(droppable);
  }

  public void drag(final Element draggable, final Event e) {
    DraggableHandler draggableHandler = DraggableHandler.getInstance(draggable);
    Collection<Element> droppables = getDroppablesByScope(draggableHandler.getOptions().getScope());
    if (droppables == null || droppables.size() == 0) {
      return;
    }

    for (Element droppable : droppables) {
      DroppableHandler dropHandler = DroppableHandler.getInstance(droppable);
      dropHandler.drag(droppable, draggable, e);
    }
  }

  public boolean drop(final Element draggable, final Event e) {
    boolean dropped = false;
    DraggableHandler draggableHandler = DraggableHandler.getInstance(draggable);
    Collection<Element> droppables = getDroppablesByScope(draggableHandler.getOptions().getScope());
    if (droppables == null || droppables.size() == 0) {
      return false;
    }

    for (Element droppable : droppables) {
      DroppableHandler droppableHandler = DroppableHandler.getInstance(droppable);
      dropped |= droppableHandler.drop(droppable, draggable, e, dropped);
    }

    return dropped;
  }

  public Element getCurrentDraggable() {
    return currentDraggable;
  }

  public Collection<Element> getDroppablesByScope(final String scope) {
    return droppablesByScope.get(scope);
  }

  public void initialize(final Element draggable, final Event e) {
    DraggableHandler draggableHandler = DraggableHandler.getInstance(draggable);
    Collection<Element> droppables = getDroppablesByScope(draggableHandler.getOptions().getScope());

    if (droppables == null || droppables.size() == 0) {
      return;
    }

    Element top = draggable;
    while (top != null && top != SimpleQuery.body) {
      if (top.getClassName().contains(Droppable.CssClassNames.GWTQUERY_DROPPABLE)) {
        break;
      }
      top = top.getParentElement();
    }

    for (Element droppable : droppables) {
      DroppableHandler droppableHandler = DroppableHandler.getInstance(droppable);
      droppableHandler.reset();
      DroppableOptions droppableOptions = droppableHandler.getOptions();
      AcceptFunction accept = droppableOptions.getAccept();
      if (droppableOptions.isDisabled()
          || (accept != null && !accept.acceptDrop(new DragAndDropContext(draggable, droppable)))) {
        continue;
      }

      if (top == droppable) {
        continue;
      }

      droppableHandler.setVisible(!"none".equals(droppable.getStyle().getDisplay()));

      if (droppableHandler.isVisible()) {
        droppableHandler.setDroppableOffset(droppable);
        droppableHandler.setDroppableDimension(droppable);
        if (e == null || e.getTypeInt() == ONMOUSEDOWN) {
          droppableHandler.activate(droppable, e);
        }
      }
    }
  }

  public boolean isHandleDroppable() {
    return droppableOnly;
  }

  public void setCurrentDraggable(final Element currentDraggable) {
    this.currentDraggable = currentDraggable;
  }

  public void setDraggable() {
    droppableOnly = false;
  }

  public void setDroppable() {
    droppableOnly = true;
  }

}
