package com.goodow.web.reader.client.droppable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HasHandlers;

public class Droppable extends SimpleQuery {

  public static interface CssClassNames {
    String GWTQUERY_DROPPABLE = "gwtQuery-droppable";
    String GWTQUERY_DROPPABLE_DISABLED = "gwtQuery-droppable-disabled";
  }

  public static final String DROPPABLE_HANDLER_KEY = "droppableHandler";

  static {
    SimpleQuery.registerPlugin(Droppable.class, new Plugin<Droppable>() {
      @Override
      public Droppable init(final SimpleQuery sq) {
        return new Droppable(sq);
      }
    });
  }

  public static final Class<Droppable> Droppable = Droppable.class;

  public Droppable(final SimpleQuery sq) {
    super(sq);
  }

  public void changeScope(final String oldScope, final String newScope) {
    DragAndDropManager ddm = DragAndDropManager.getInstance();
    ddm.changeScope(oldScope, newScope);
  }

  public Droppable destroy() {
    DragAndDropManager ddm = DragAndDropManager.getInstance();
    for (Element e : elements()) {
      DroppableHandler infos = DroppableHandler.getInstance(e);
      ddm.getDroppablesByScope(infos.getOptions().getScope()).remove(e);
      e.removeClassName(CssClassNames.GWTQUERY_DROPPABLE);
      e.removeClassName(CssClassNames.GWTQUERY_DROPPABLE_DISABLED);
      SimpleQuery.q(e).removeData(DROPPABLE_HANDLER_KEY);
    }
    if (ddm.scopeIsClear()) {
      ddm.setDraggable();
    }
    return this;
  }

  public Droppable droppable(final DroppableOptions droppableOptions, final HasHandlers eventBus) {

    DragAndDropManager ddm = DragAndDropManager.getInstance();

    for (Element e : elements()) {
      DroppableHandler handler = new DroppableHandler(droppableOptions, eventBus);
      handler.setDroppableDimension(e);
      SimpleQuery.q(e).data(DROPPABLE_HANDLER_KEY, handler);

      ddm.addDroppable(e, droppableOptions.getScope());
      e.addClassName(CssClassNames.GWTQUERY_DROPPABLE);
      ddm.setDroppable();
    }

    return this;
  }

}
