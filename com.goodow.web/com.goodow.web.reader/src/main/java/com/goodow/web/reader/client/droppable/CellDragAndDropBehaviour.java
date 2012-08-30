package com.goodow.web.reader.client.droppable;

public interface CellDragAndDropBehaviour {

  public class CellDragOnlyBehaviour implements CellDragAndDropBehaviour {

    @Override
    public boolean isDraggable() {
      return true;
    }

    @Override
    public boolean isDroppable() {
      return false;
    }

  }

  public class CellDropOnlyBehaviour implements CellDragAndDropBehaviour {

    @Override
    public boolean isDraggable() {
      return false;
    }

    @Override
    public boolean isDroppable() {
      return true;
    }

  }

  boolean isDraggable();

  boolean isDroppable();
}
