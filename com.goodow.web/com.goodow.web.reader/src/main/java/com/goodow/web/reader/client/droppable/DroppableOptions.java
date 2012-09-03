package com.goodow.web.reader.client.droppable;

public class DroppableOptions {

  public static interface AcceptFunction {

    public boolean acceptDrop(DragAndDropContext context);

  }

  public static enum DroppableTolerance {
    FIT, INTERSECT, POINTER, TOUCH;
  }

  public static final String DEFAULT_SCOPE = "default";

  public static AcceptFunction ACCEPT_ALL = new AcceptFunction() {
    @Override
    public boolean acceptDrop(final DragAndDropContext context) {
      return true;
    }
  };

  private String scope;
  private AcceptFunction accept = ACCEPT_ALL;
  private boolean disabled;
  private String activeClass = null;
  private String draggableHoverClass = null;
  private String droppableHoverClass = null;
  private DroppableTolerance tolerance = DroppableTolerance.INTERSECT;

  public AcceptFunction getAccept() {
    return accept;
  }

  public String getActiveClass() {
    return activeClass;
  }

  public String getDraggableHoverClass() {
    return draggableHoverClass;
  }

  public String getDroppableHoverClass() {
    return droppableHoverClass;
  }

  public String getScope() {
    return scope;
  }

  public DroppableTolerance getTolerance() {
    return tolerance;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setAccept(final AcceptFunction acceptFunction) {
    this.accept = acceptFunction;
  }

  public void setActiveClass(final String activeClass) {
    this.activeClass = activeClass;
  }

  public void setDisabled(final boolean disabled) {
    this.disabled = disabled;
  }

  public void setDraggableHoverClass(final String draggableHoverClass) {
    this.draggableHoverClass = draggableHoverClass;
  }

  public void setDroppableHoverClass(final String droppableHoverClass) {
    this.droppableHoverClass = droppableHoverClass;
  }

  public void setScope(final String scope) {
    this.scope = scope;
  }

  public void setTolerance(final DroppableTolerance tolerance) {
    this.tolerance = tolerance;
  }

}
