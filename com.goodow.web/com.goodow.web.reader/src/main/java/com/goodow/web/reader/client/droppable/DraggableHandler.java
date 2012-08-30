package com.goodow.web.reader.client.droppable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;

public class DraggableHandler {

  public static final String DRAGGABLE_HANDLER_KEY = "draggableHandler";

  public static DraggableHandler getInstance(final Element draggable) {
    return SimpleQuery.q(draggable).data(DRAGGABLE_HANDLER_KEY);
  }

  private Element helper;

  private boolean cancelHelperRemoval = false;
  private DraggableOptions options;

  private int absPositionLeft;
  private int absPositionTop;
  private int nowPageX;
  private int nowPageY;
  private int helperClientTop;
  private int helperClientLeft;

  DraggableHandler(final DraggableOptions options) {
    this.options = options;
  }

  public void cacheHelperSize() {
    if (getHelper() == null) {
      return;
    }
    // helperClientHeight = getHelper().getClientHeight();
    // helperClientWidth = getHelper().getClientWidth();
  }

  public void clear() {
    if (getHelper() == null) {
      return;
    }
    getHelper().removeClassName(Draggable.CssClassNames.GWT_DRAGGABLE_DRAGGING);
    if (!cancelHelperRemoval) {
      getHelper().removeFromParent();
    }
    helper = null;
    cancelHelperRemoval = false;

  }

  public void createHelper(final Element draggable) {
    helper = options.getHelperType().createHelper(draggable, options.getHelper());
    if (!isElementAttached(getHelper())) {
      RootPanel.get().getElement().appendChild(getHelper());
      // draggable.getParentElement().appendChild(getHelper());
      // if ("parent".equals(options.getAppendTo())) {
      // helper.appendTo(draggable.getParentNode());
      // } else {
      // helper.appendTo(options.getAppendTo());
      // }
    }

    getHelper().getStyle().setPosition(Position.ABSOLUTE);
  }

  public void generatePosition(final Event e) {
    int pageX = pageX(e);
    int pageY = pageY(e);

    if (nowPageX == 0) {
      nowPageX = pageX;
    }
    if (nowPageY == 0) {
      nowPageY = pageY;
    }

    helperClientTop = absPositionTop + pageY - nowPageY;
    helperClientLeft = absPositionLeft + pageX - nowPageX;
  }

  public Element getHelper() {
    return helper;
  }

  public void initHelperPostition() {
    helperClientTop = absPositionTop;
    helperClientLeft = absPositionLeft;
  }

  public void initialize(final Element element) {

    absPositionLeft = element.getAbsoluteLeft() - 6;
    absPositionTop = element.getAbsoluteTop() - 6;
  }

  public void moveHelper(final boolean isStart) {
    if (isStart) {
      return;
    }
    helper.getStyle().setLeft(helperClientLeft, Unit.PX);
    helper.getStyle().setTop(helperClientTop, Unit.PX);
  }

  public final int pageX(final Event e) {
    return e.getClientX() + SimpleQuery.document.getScrollLeft();
  }

  public final int pageY(final Event e) {
    return e.getClientY() + SimpleQuery.document.getScrollTop();
  }

  public void recovery() {
    nowPageX = 0;
    nowPageY = 0;
  }

  private boolean isElementAttached(final Element helper) {
    Element parentElement = helper.getParentElement();
    while (parentElement != null) {
      if (parentElement == SimpleQuery.body) {
        return true;
      }
      parentElement.getParentElement();
    }

    return false;
  }

}
