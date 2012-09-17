package com.goodow.web.reader.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ColumnVisiableEvent extends GwtEvent<ColumnVisiableEvent.Handle> {

  public static interface Handle extends EventHandler {
    public void onColumnVisiable(ColumnVisiableEvent columnVisiableEvent);
  }

  public static Type<Handle> TYPE = new Type<Handle>();

  public static ColumnVisiableEvent fire(final HasHandlers source,
      final ColumnEntity<?> columnEntity) {
    ColumnVisiableEvent event = new ColumnVisiableEvent(columnEntity);
    if (TYPE != null) {
      source.fireEvent(event);
    }
    return event;
  }

  private ColumnEntity<?> columnEntity;

  public ColumnVisiableEvent(final ColumnEntity<?> columnEntity) {
    this.columnEntity = columnEntity;
  }

  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<Handle> getAssociatedType() {
    return TYPE;
  }

  public ColumnEntity<?> getColumnEntity() {
    return columnEntity;
  }

  @Override
  protected void dispatch(final Handle handler) {
    handler.onColumnVisiable(this);
  }
}
