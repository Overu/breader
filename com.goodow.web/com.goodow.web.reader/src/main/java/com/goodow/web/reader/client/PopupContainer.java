package com.goodow.web.reader.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class PopupContainer extends SimplePanel {

  private Style mainStyle;
  private Style containerStyle;

  private SimplePanel container;

  public PopupContainer() {
    container = new SimplePanel();

    Element mainElm = this.getElement();
    mainElm.setId("PopupContainer");

    Element containerElm = container.getElement();
    containerStyle = containerElm.getStyle();
    containerStyle.setPosition(Position.ABSOLUTE);
    containerStyle.setZIndex(100);

    mainStyle = mainElm.getStyle();
    mainStyle.setPosition(Position.ABSOLUTE);
    setFullScreen();

    this.setWidget(container);
    RootPanel.get().add(this);

  }

  public <H extends EventHandler> HandlerRegistration addContainerHandle(final H handler,
      final DomEvent.Type<H> type) {
    return container.addDomHandler(handler, type);
  }

  public void hide() {
    if (this.getWidget() == null) {
      return;
    }
    containerStyle.clearTop();
    containerStyle.clearLeft();
    setFullScreen();
    container.clear();
  }

  public void show(final Widget attchPanel) {
    int absoluteLeft = attchPanel.getAbsoluteLeft();
    int absoluteTop = attchPanel.getAbsoluteTop() + attchPanel.getOffsetHeight();
    mainStyle.setZIndex(99);
    containerStyle.setTop(absoluteTop, Unit.PX);
    containerStyle.setLeft(absoluteLeft, Unit.PX);
    if (attchPanel instanceof TopBarButton) {
      clearFullScreen();
      container.setWidget(((TopBarButton) attchPanel).getPopupComponent());
    }
  }

  private void clearFullScreen() {
    mainStyle.clearTop();
    mainStyle.clearLeft();
    mainStyle.clearRight();
    mainStyle.clearBottom();
  }

  private void setFullScreen() {
    mainStyle.setTop(0, Unit.PX);
    mainStyle.setBottom(0, Unit.PX);
    mainStyle.setLeft(0, Unit.PX);
    mainStyle.setRight(0, Unit.PX);
    mainStyle.setZIndex(-1);
  }

}
