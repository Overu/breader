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
      final ColumnEntity<?> columnEntity, final boolean checked) {
    ColumnVisiableEvent event = new ColumnVisiableEvent(columnEntity, checked);
    if (TYPE != null) {
      source.fireEvent(event);
    }
    return event;
  }

  private ColumnEntity<?> columnEntity;
  private boolean checked;

  public ColumnVisiableEvent(final ColumnEntity<?> columnEntity, final boolean checked) {
    this.columnEntity = columnEntity;
    this.checked = checked;
  }

  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<Handle> getAssociatedType() {
    return TYPE;
  }

  public ColumnEntity<?> getColumnEntity() {
    return columnEntity;
  }

  public boolean isChecked() {
    return checked;
  }

  public void setChecked(final boolean checked) {
    this.checked = checked;
  }

  @Override
  protected void dispatch(final Handle handler) {
    handler.onColumnVisiable(this);
  }
}
