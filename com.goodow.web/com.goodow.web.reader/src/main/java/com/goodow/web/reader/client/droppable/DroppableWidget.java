package com.goodow.web.reader.client.droppable;

import com.goodow.web.reader.client.droppable.DroppableOptions.AcceptFunction;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class DroppableWidget<T extends Widget> extends Composite {

  private final static String DROPPABLE_WIDGET_KEY = "__droppableWidget";

  public static DroppableWidget<?> get(final Element e) {
    return SimpleQuery.q(e).data(DROPPABLE_WIDGET_KEY, DroppableWidget.class);
  }

  private EventBus dropHandlerManager;

  private DroppableOptions options;

  public DroppableWidget(final T w) {
    this(w, new DroppableOptions(), DraggableOptions.DEFAULT_SCOPE);
  }

  public DroppableWidget(final T w, final DroppableOptions options) {
    this(w, options, DraggableOptions.DEFAULT_SCOPE);
  }

  public DroppableWidget(final T w, final DroppableOptions options, final String scope) {
    initWidget(w);
    this.options = options;
    this.options.setScope(scope);
  }

  public void setAccept(final AcceptFunction acceptFunction) {
    if (acceptFunction != null) {
      options.setAccept(acceptFunction);
    } else {
      options.setAccept(DroppableOptions.ACCEPT_ALL);
    }
  }

  public void setDisabled(final boolean disabled) {
    options.setDisabled(disabled);
  }

  public void setDraggableHoverClass(final String draggableHoverClass) {
    options.setDraggableHoverClass(draggableHoverClass);
  }

  protected final <H extends EventHandler> HandlerRegistration addDropHandler(final H handler,
      final Type<H> type) {
    return ensureDropHandlers().addHandler(type, handler);
  }

  protected EventBus ensureDropHandlers() {
    return dropHandlerManager == null ? dropHandlerManager = new SimpleEventBus()
        : dropHandlerManager;
  }

  protected EventBus getDropHandlerManager() {
    return dropHandlerManager;
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    SimpleQuery.q(getElement()).as(Droppable.Droppable).droppable(options, ensureDropHandlers())
        .data(DROPPABLE_WIDGET_KEY, this);
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    SimpleQuery.q(getElement()).as(Droppable.Droppable).destroy().removeData(DROPPABLE_WIDGET_KEY);
  }

}
