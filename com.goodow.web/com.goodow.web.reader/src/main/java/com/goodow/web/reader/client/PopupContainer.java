package com.goodow.web.reader.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

  public enum Loction {
    OVER, BELOW, LEFT, RIGHT, CENTER
  }

  private Style mainStyle;
  private Style containerStyle;

  private SimplePanel container;

  int absoluteLeft;
  int absoluteTop;

  private boolean isPopup = true;

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

    this.addDomHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        if (!isPopup) {
          return;
        }
        hide();
      }
    }, ClickEvent.getType());

  }

  public <H extends EventHandler> HandlerRegistration addContainerHandle(final H handler,
      final DomEvent.Type<H> type) {
    return container.addDomHandler(handler, type);
  }

  public void dialog(final Widget dialogWidget) {
    dialog(dialogWidget, Loction.CENTER);
  }

  public void dialog(final Widget dialogWidget, final Loction loction) {
    isPopup = false;
    mainStyle.setBackgroundColor("rgba(0, 0, 0, 0.6)");
    showBase(this.getElement(), dialogWidget, loction);
  }

  public void hide() {
    if (this.getWidget() == null) {
      return;
    }
    containerStyle.clearTop();
    containerStyle.clearLeft();
    setFullScreen();
    container.clear();
    isPopup = true;
  }

  public void show(final Element attchElm, final Widget popupPanel) {
    showBase(attchElm, popupPanel, Loction.BELOW);
  }

  public void show(final Element attchElm, final Widget popupPanel, final Loction loction) {
    showBase(attchElm, popupPanel, loction);
  }

  public void show(final Widget attchPanel, final Widget popupPanel) {
    showBase(attchPanel.getElement(), popupPanel, Loction.BELOW);
    if (attchPanel instanceof TopBarButton) {
      container.clear();
      clearFullScreen();
      container.setWidget(((TopBarButton) attchPanel).getPopupComponent());
    }
  }

  public void show(final Widget attchPanel, final Widget popupPanel, final Loction loction) {
    showBase(attchPanel.getElement(), popupPanel, loction);
  }

  private void calculateLoction(final Loction loction, final Element attchElm,
      final Widget popupPanel) {
    switch (loction) {
      case OVER:
        absoluteTop -= popupPanel.getOffsetHeight();
        absoluteLeft -= popupPanel.getOffsetWidth() / 2;
        break;
      case BELOW:
        absoluteTop += attchElm.getOffsetHeight();
        break;
      case LEFT:

        break;
      case RIGHT:

        break;
      case CENTER:
        int clientHeight = attchElm.getClientHeight() / 2;
        int clientWidth = attchElm.getClientWidth() / 2;
        int offsetHeight = popupPanel.getOffsetHeight();
        int offsetWidth = popupPanel.getOffsetWidth() / 2;
        absoluteTop = clientHeight - offsetHeight;
        absoluteLeft = clientWidth - offsetWidth;
        break;

      default:
        break;
    }
  }

  private void clearFullScreen() {
    mainStyle.clearTop();
    mainStyle.clearLeft();
    mainStyle.clearRight();
    mainStyle.clearBottom();
  }

  private void setFullScreen() {
    mainStyle.clearBackgroundColor();
    mainStyle.setTop(0, Unit.PX);
    mainStyle.setBottom(0, Unit.PX);
    mainStyle.setLeft(0, Unit.PX);
    mainStyle.setRight(0, Unit.PX);
    mainStyle.setZIndex(-1);
  }

  private void showBase(final Element attchElm, final Widget popupPanel, final Loction loction) {
    absoluteLeft = attchElm.getAbsoluteLeft();
    absoluteTop = attchElm.getAbsoluteTop();
    container.setWidget(popupPanel);
    calculateLoction(loction, attchElm, popupPanel);
    mainStyle.setZIndex(99);
    containerStyle.setTop(absoluteTop, Unit.PX);
    containerStyle.setLeft(absoluteLeft, Unit.PX);
  }
}
