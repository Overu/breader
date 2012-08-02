package com.goodow.web.reader.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ListDropDownPanel extends DropDownPanel {

  interface Binder extends UiBinder<Widget, ListDropDownPanel> {
  }

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  HTMLPanel container;

  public ListDropDownPanel() {
    main.add(binder.createAndBindUi(this));
  }

  @Override
  public Widget addChild(final IsWidget isWidget, final ClickHandler clickHandler) {
    Widget widget = isWidget.asWidget();
    if (widget instanceof Image) {
      widget = new SimplePanel(widget);
    }
    container.add(widget);
    if (clickHandler == null) {
      return widget;
    }
    widget.addDomHandler(clickHandler, ClickEvent.getType());
    return widget;
  }

  @Override
  protected void start() {

  }
}
