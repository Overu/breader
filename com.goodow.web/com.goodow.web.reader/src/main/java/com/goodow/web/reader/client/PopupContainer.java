package com.goodow.web.reader.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class PopupContainer extends SimplePanel {

  private Style style;

  public PopupContainer() {
    Element element = this.getElement();
    element.setId("PopupContainer");

    style = element.getStyle();
    style.setPosition(Position.ABSOLUTE);
    style.setZIndex(100);

    RootPanel.get().add(this);

  }

  public void hide() {
    style.clearTop();
    style.clearLeft();
    clear();
  }

  public void show(final Widget attchPanel) {
    int absoluteLeft = attchPanel.getAbsoluteLeft();
    int absoluteTop = attchPanel.getAbsoluteTop() + attchPanel.getOffsetHeight();
    style.setTop(absoluteTop, Unit.PX);
    style.setLeft(absoluteLeft, Unit.PX);
    if (attchPanel instanceof TopBarButton) {
      this.setWidget(((TopBarButton) attchPanel).getPopupComponent());
    }
  }

}
