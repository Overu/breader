package com.goodow.web.reader.client;

import com.goodow.web.reader.client.HeadTypeEvent.HeadType;
import com.goodow.web.reader.client.style.ReadResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class TopBarButton extends Composite {

  interface Binder extends UiBinder<Widget, TopBarButton> {
  }

  private static Binder binder = GWT.create(Binder.class);

  private DropDownPanel popupComponent;

  private boolean isMouseMove = false;

  @UiField
  HTMLPanel title;

  public TopBarButton() {
    initWidget(binder.createAndBindUi(this));
  }

  public HandlerRegistration addHeadTypeHandler(final HeadTypeEvent.HeadTypeHandle handler) {
    return addHandler(handler, HeadTypeEvent.TYPE);
  }

  public void addHover() {
    addStyleName(ReadResources.INSTANCE().css().topBarButtonHOVER());
  }

  public DropDownPanel getPopupComponent() {
    return popupComponent;
  }

  public void removeHover() {
    removeStyleName(ReadResources.INSTANCE().css().topBarButtonHOVER());
  }

  public void setIcon(final ImageResource icon) {
    title.add(new Image(icon));
  }

  public void setPopupComponent(final DropDownPanel popupComponent) {
    title.getElement().setAttribute("dropdown", "");
    this.popupComponent = popupComponent;
    this.addDomHandler(new MouseMoveHandler() {

      @Override
      public void onMouseMove(final MouseMoveEvent event) {
        if (isMouseMove) {
          return;
        }
        isMouseMove = true;
        fireEvent(new HeadTypeEvent(HeadType.MOUSEOVER, TopBarButton.this));
      }
    }, MouseMoveEvent.getType());

    this.addDomHandler(new MouseOutHandler() {

      @Override
      public void onMouseOut(final MouseOutEvent event) {
        isMouseMove = false;
        fireEvent(new HeadTypeEvent(HeadType.MOUSEOUT, TopBarButton.this));
      }
    }, MouseOutEvent.getType());
  }

  @Override
  public void setTitle(final String text) {
    title.getElement().setInnerHTML(text);
  }
}
