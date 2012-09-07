package com.goodow.web.reader.client.droppable;

import com.goodow.web.reader.client.editgrid.Function;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.Event;

public class Draggable extends SimpleQuery {

  public static interface CssClassNames {
    String GWT_DRAGGABLE = "gwtQuery-draggable";
    String GWT_DRAGGABLE_DISABLED = "gwtQuery-draggable-disabled";
    String GWT_DRAGGABLE_DRAGGING = "gwtQuery-draggable-dragging";

  }

  static {
    SimpleQuery.registerPlugin(Draggable.class, new Plugin<Draggable>() {
      @Override
      public Draggable init(final SimpleQuery sq) {
        return new Draggable(sq);
      }
    });
  }

  public static final Class<Draggable> Draggable = Draggable.class;
  private static final String PLUGINNAME = "draggable";
  public static final int NEEDEDDISTANCE = 2;

  private static void trigger(final GwtEvent<?> e, final HasHandlers handlerManager) {
    if (handlerManager != null && e != null) {
      handlerManager.fireEvent(e);
    }
  }

  private boolean preventClickEvent = false;

  private boolean mouseStarted = false;
  protected HasHandlers eventBus;
  private DraggableHandler currentDragHandler;

  private Event mouseDownEvent;

  protected Draggable(final SimpleQuery sq) {
    super(sq);
  }

  public Draggable destroy() {
    for (Element e : elements()) {
      SimpleQuery.q(e).removeData(DraggableHandler.DRAGGABLE_HANDLER_KEY);
      e.removeClassName(CssClassNames.GWT_DRAGGABLE);
      e.removeClassName(CssClassNames.GWT_DRAGGABLE_DISABLED);
      e.removeClassName(CssClassNames.GWT_DRAGGABLE_DRAGGING);
    }
    destroyMouseHandler();
    return this;
  }

  public Draggable draggable(final HasHandlers eventBus, final DraggableOptions darggableOptions) {
    this.eventBus = eventBus;

    initMouseHandler();

    for (Element e : elements()) {
      e.addClassName(CssClassNames.GWT_DRAGGABLE);
      DraggableHandler handler = new DraggableHandler(darggableOptions);
      q(e).data(DraggableHandler.DRAGGABLE_HANDLER_KEY, handler);
    }

    return this;
  }

  protected void destroyMouseHandler() {
    as(Events.Events).unbind(Event.ONMOUSEDOWN | Event.ONCLICK, PLUGINNAME);
  }

  protected void initMouseHandler() {
    for (final Element e : elements()) {
      SimpleQuery.q(e).as(Events.Events).bind(Event.ONMOUSEDOWN, PLUGINNAME, (Object) null,
          new Function() {

            @Override
            public boolean f(final Event event) {
              return mouseDown(e, event);
            }

          }).bind(Event.ONCLICK, PLUGINNAME, (Object) null, new Function() {

        @Override
        public boolean f(final Event event) {
          preventClickEvent |= !mouseClick(e, event);

          if (preventClickEvent) {

            preventClickEvent = false;
            event.stopPropagation();
            event.preventDefault();
            return false;
          }

          return true;
        }

      });
    }
  }

  @SuppressWarnings("unused")
  protected boolean mouseClick(final Element element, final Event event) {
    return true;
  }

  protected boolean mouseDown(final Element element, final Event event) {

    if (isEventAlreadyHandled(event)) {
      return false;
    }
    if (mouseStarted) {
      mouseUp(element, event);
    }

    reset(event);

    if (distanceConditionMet(event)) {
      mouseStarted = mouseStart(element, event);
      if (!mouseStarted) {
        event.preventDefault();
        return true;
      }
    }

    bindOtherMouseEvent(element);

    event.preventDefault();

    markEventAsHandled(event);

    return true;
  }

  protected boolean mouseMove(final Element element, final Event event) {
    if (mouseStarted) {
      event.preventDefault();
      return mouseDragImpl(element, getHandler(element), event, false);
    }

    if (distanceConditionMet(event)) {
      mouseStarted = mouseStart(element, mouseDownEvent);
      if (mouseStarted) {
        mouseDragImpl(element, getHandler(element), event, false);
      } else {
        mouseUp(element, event);
      }
    }

    return !mouseStarted;
  }

  protected boolean mouseStart(final Element draggable, final Event event) {
    reset();
    DraggableHandler dragHandler = getHandler(draggable);

    dragHandler.createHelper(draggable);

    if (getDragAndDropManager().isHandleDroppable()) {
      getDragAndDropManager().setCurrentDraggable(draggable);
    }

    dragHandler.initialize(draggable, event);

    try {
      trigger(new DragStartEvent(draggable));
    } catch (UmbrellaException e) {
      for (Throwable t : e.getCauses()) {
        if (t instanceof RuntimeException) {
          mouseStop(draggable);
          return false;
        }
      }
    }

    dragHandler.cacheHelperSize();

    if (getDragAndDropManager().isHandleDroppable()) {
      getDragAndDropManager().initialize(draggable, event);
    }

    getHelper(draggable).addClassName(CssClassNames.GWT_DRAGGABLE_DRAGGING);

    mouseDragImpl(draggable, dragHandler, event, true);
    return true;
  }

  protected boolean mouseStop(final Element draggable) {

    final DraggableHandler handler = getHandler(draggable);

    if (getDragAndDropManager().isHandleDroppable()) {
      getDragAndDropManager().drop(draggable);
    }

    if (draggable == null) {
      return false;
    }

    handler.recovery();
    handler.clear();

    return false;
  }

  protected boolean mouseUp(final Element element, final Event event) {
    unbindOtherMouseEvent();
    if (mouseStarted) {
      mouseStarted = false;
      preventClickEvent = (event.getCurrentEventTarget() == mouseDownEvent.getCurrentEventTarget());
      mouseStop(element);
    }
    return false;
  }

  private void bindOtherMouseEvent(final Element element) {

    q(document).as(Events.Events).bind(Event.ONMOUSEMOVE, PLUGINNAME, (Object) null,
        new Function() {
          @Override
          public boolean f(final com.google.gwt.user.client.Event e) {
            mouseMove(element, e);
            return false;
          }
        }).bind(Event.ONMOUSEUP, PLUGINNAME, (Object) null, new Function() {
      @Override
      public boolean f(final com.google.gwt.user.client.Event e) {
        mouseUp(element, e);
        return false;
      }
    });
  }

  private boolean distanceConditionMet(final Event event) {
    // 默认的移动距离
    int mouseDownX = mouseDownEvent.getClientX();
    int mouseDownY = mouseDownEvent.getClientY();
    int xMouseDistance = Math.abs(mouseDownX - event.getClientX());
    int yMouseDistance = Math.abs(mouseDownY - event.getClientY());

    int mouseDistance =
        (int) Math.sqrt(xMouseDistance * xMouseDistance + yMouseDistance * yMouseDistance);
    return mouseDistance >= NEEDEDDISTANCE;
  }

  private DragAndDropManager getDragAndDropManager() {
    return DragAndDropManager.getInstance();
  }

  private DraggableHandler getHandler(final Element draggable) {

    if (currentDragHandler == null) {
      currentDragHandler =
          q(draggable).data(DraggableHandler.DRAGGABLE_HANDLER_KEY, DraggableHandler.class);
    }
    return currentDragHandler;
  }

  private Element getHelper(final Element draggable) {
    DraggableHandler handler = getHandler(draggable);
    return handler != null ? handler.getHelper() : null;
  }

  private native boolean isEventAlreadyHandled(Event event)/*-{
                                                             var result = event.mouseHandled ? event.mouseHandled : false;
                                                             return result;
                                                             }-*/;

  private native void markEventAsHandled(Event event)/*-{
                                                     event.mouseHandled = true;
                                                     }-*/;

  private boolean mouseDragImpl(final Element draggable, final DraggableHandler dragHandler,
      final Event event, final boolean isStart) {
    if (isStart) {
      dragHandler.initHelperPostition();
    } else {
      dragHandler.generatePosition(event);
    }

    dragHandler.moveHelper(isStart);

    if (getDragAndDropManager().isHandleDroppable()) {
      getDragAndDropManager().drag(draggable);
    }
    return false;
  }

  private void reset() {
    currentDragHandler = null;
  }

  private void reset(final Event mouseDownEvent) {
    this.mouseDownEvent = mouseDownEvent;
  }

  private void trigger(final GwtEvent<?> e) {
    trigger(e, eventBus);
  }

  private void unbindOtherMouseEvent() {
    q(document).as(Events.Events).unbind((Event.ONMOUSEUP | Event.ONMOUSEMOVE), PLUGINNAME);
  }
}
