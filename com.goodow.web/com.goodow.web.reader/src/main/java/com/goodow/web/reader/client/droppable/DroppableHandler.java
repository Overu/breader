package com.goodow.web.reader.client.droppable;

import com.goodow.web.reader.client.droppable.DroppableOptions.AcceptFunction;
import com.goodow.web.reader.client.droppable.DroppableOptions.DroppableTolerance;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class DroppableHandler {

  private static enum PositionStatus {
    IS_OUT, IS_OVER;
  }

  public static final String DROPPABLE_HANDLER_KEY = "droppableHandler";

  public static DroppableHandler getInstance(final Element droppable) {
    return SimpleQuery.q(droppable).data(DROPPABLE_HANDLER_KEY, DroppableHandler.class);
  }

  private DroppableOptions options;

  private HasHandlers eventBus;
  private boolean visible = false;
  private boolean greedyChild = false;

  private int absoluteLeft;
  private int absoluteTop;
  private int offsetWidth;
  private int offsetHeight;

  private boolean isOut = true;
  private boolean isOver = false;

  public DroppableHandler(final DroppableOptions options, final HasHandlers eventBus) {
    this.setOptions(options);
    this.eventBus = eventBus;

  }

  public void activate(final Element droppable) {
    if (options.getActiveClass() != null) {
      droppable.addClassName(options.getActiveClass());
    }
    Element draggable = DragAndDropManager.getInstance().getCurrentDraggable();
    if (draggable != null) {
      if (options.getDraggableHoverClass() != null) {
        SimpleQuery.q(draggable).data(options.getDraggableHoverClass(), new Integer(0));
      }
      // DragAndDropContext ctx = new DragAndDropContext(draggable, droppable);
      // trigger(new ActivateDroppableEvent(ctx), options.getOnActivate(), ctx);
    }
  }

  public void deactivate(final Element droppable) {
    if (options.getActiveClass() != null) {
      droppable.removeClassName(options.getActiveClass());
    }
    if (options.getDroppableHoverClass() != null) {
      droppable.removeClassName(options.getDroppableHoverClass());
    }
    Element draggable = DragAndDropManager.getInstance().getCurrentDraggable();
    if (draggable != null) {
      if (options.getDraggableHoverClass() != null) {
        DraggableHandler dragHandler = DraggableHandler.getInstance(draggable);
        dragHandler.getHelper().removeClassName(options.getDraggableHoverClass());
        SimpleQuery.q(draggable).removeData(options.getDraggableHoverClass());
      }

      // DragAndDropContext ctx = new DragAndDropContext(draggable, droppable);
      // trigger(new DeactivateDroppableEvent(ctx), options.getOnDeactivate(), ctx);
    }
  }

  public void drag(final Element droppable, final Element draggable) {
    if (options.isDisabled() || greedyChild || !visible) {
      return;
    }

    boolean isIntersect = intersect(draggable);
    PositionStatus c = null;

    if (!isIntersect && isOver) {
      c = PositionStatus.IS_OUT;
    } else if (isIntersect && !isOver) {
      c = PositionStatus.IS_OVER;
    }
    if (c == null) {
      return;
    }

    // DroppableHandler parentDroppableHandler = null;

    if (c == PositionStatus.IS_OUT) {
      isOut = true;
      isOver = false;
      out(droppable, draggable);
    } else {
      isOver = true;
      isOut = false;
      over(droppable, draggable);
    }
  }

  public boolean drop(final Element droppable, final Element draggable) {
    if (options == null) {
      return false;
    }

    boolean drop = false;

    if (!options.isDisabled() && visible) {
      if (intersect(draggable) && !checkChildrenIntersection(droppable, draggable)
          && isDraggableAccepted(droppable, draggable)) {

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

          @Override
          public void execute() {
            trigger(new DropEvent(draggable, droppable));
          }
        });
        drop = true;
      }
      if (drop || isDraggableAccepted(droppable, draggable)) {
        setOut(true);
        setOver(false);
        deactivate(droppable);
      }
    }
    return drop;
  }

  public DroppableOptions getOptions() {
    return options;
  }

  public boolean isOut() {
    return isOut;
  }

  public boolean isOver() {
    return isOver;
  }

  public boolean isVisible() {
    return visible;
  }

  public void out(final Element droppable, final Element currentDraggable) {
    if (currentDraggable == null || currentDraggable == droppable) {
      return;
    }
    if (isDraggableAccepted(droppable, currentDraggable)) {
      if (options.getDroppableHoverClass() != null) {
        droppable.removeClassName(options.getDroppableHoverClass());
      }
      if (options.getDraggableHoverClass() != null) {
        Integer counter =
            SimpleQuery.q(currentDraggable).data(options.getDraggableHoverClass(), Integer.class);
        SimpleQuery.q(currentDraggable).data(options.getDraggableHoverClass(),
            new Integer(--counter));
        if (counter == 0) {
          DraggableHandler dragHandler = DraggableHandler.getInstance(currentDraggable);
          dragHandler.getHelper().removeClassName(options.getDraggableHoverClass());
        }
      }
    }
  }

  public void over(final Element droppable, final Element currentDraggable) {
    if (currentDraggable == null || currentDraggable == droppable) {
      return;
    }

    if (isDraggableAccepted(droppable, currentDraggable)) {
      if (options.getDroppableHoverClass() != null) {
        droppable.addClassName(options.getDroppableHoverClass());
      }
      if (options.getDraggableHoverClass() != null) {
        DraggableHandler dragHandler = DraggableHandler.getInstance(currentDraggable);
        dragHandler.getHelper().addClassName(options.getDraggableHoverClass());
        Integer counter =
            SimpleQuery.q(currentDraggable).data(options.getDraggableHoverClass(), Integer.class);
        SimpleQuery.q(currentDraggable).data(options.getDraggableHoverClass(),
            new Integer(++counter));
      }
    }
  }

  public void reset() {
    absoluteLeft = -1;
    absoluteTop = -1;
    offsetWidth = -1;
    offsetHeight = 1;
  }

  public void setDroppableDimension(final Element droppable) {
    this.offsetWidth = droppable.getOffsetWidth();
    this.offsetHeight = droppable.getOffsetHeight();
  }

  public void setDroppableOffset(final Element droppable) {
    this.absoluteLeft = droppable.getAbsoluteLeft();
    this.absoluteTop = droppable.getAbsoluteTop();
  }

  public void setOptions(final DroppableOptions options) {
    this.options = options;
  }

  public void setOut(final boolean isOut) {
    this.isOut = isOut;
  }

  public void setOver(final boolean isOver) {
    this.isOver = isOver;
  }

  public void setVisible(final boolean visible) {
    this.visible = visible;
  }

  @SuppressWarnings("unused")
  private boolean checkChildrenIntersection(final Element droppable, final Element draggable) {
    return false;
  }

  private boolean intersect(final Element draggable) {
    if ((absoluteLeft == -1 && absoluteTop == -1) || (offsetHeight == -1 && offsetWidth == -1)) {
      return false;
    }
    DraggableHandler dragHandler = DraggableHandler.getInstance(draggable);
    int draggableLeft = dragHandler.getHelperClientLeft();
    int draggableRight = draggableLeft + dragHandler.getHelper().getOffsetHeight();
    int draggableTop = dragHandler.getHelperClientTop();
    int draggableBottom = draggableTop + dragHandler.getHelper().getOffsetWidth();

    int droppableLeft = absoluteLeft;
    int droppableRight = droppableLeft + offsetWidth;
    int droppableTop = absoluteTop;
    int droppableBottom = droppableTop + offsetHeight;

    DroppableTolerance tolerance = options.getTolerance();
    switch (tolerance) {
      case FIT:
        return droppableLeft <= draggableLeft && draggableRight <= droppableRight
            && droppableTop <= draggableTop && draggableBottom <= droppableBottom;
      case INTERSECT:
        float dragHelperHalfWidth = dragHandler.getHelper().getOffsetWidth() / 2;
        float dragHelperHalfHeight = dragHandler.getHelper().getOffsetHeight() / 2;
        return droppableLeft < draggableLeft + dragHelperHalfWidth
            && droppableRight > draggableLeft + dragHelperHalfWidth
            && droppableTop < draggableTop + dragHelperHalfHeight
            && droppableBottom > draggableTop + dragHelperHalfHeight;
      case POINTER:
        int pointerLeft = draggableLeft + dragHandler.getOffsetClickLeft();
        int pointerTop = draggableTop + dragHandler.getOffsetClickTop();
        return pointerLeft > droppableLeft && pointerLeft < droppableRight
            && pointerTop > droppableTop && pointerTop < droppableBottom;

      case TOUCH:
        return ((draggableTop >= droppableTop && draggableTop <= droppableBottom)
            || (draggableBottom >= droppableTop && draggableBottom <= droppableBottom) || (draggableTop < droppableTop && draggableBottom > droppableBottom))
            && ((draggableLeft >= droppableLeft && draggableLeft <= droppableRight)
                || (draggableRight >= droppableLeft && draggableRight <= droppableRight) || (draggableLeft < droppableLeft && draggableRight > droppableRight));

      default:
        break;
    }

    return true;
  }

  private boolean isDraggableAccepted(final Element droppable, final Element draggable) {
    AcceptFunction accept = options.getAccept();
    return accept != null && accept.acceptDrop(new DragAndDropContext(draggable, droppable));
  }

  private void trigger(final GwtEvent<?> e) {

    if (eventBus != null && e != null) {
      eventBus.fireEvent(e);
    }
    // if (callback != null) {
    // callback.f(context);
    // }
  }
}
