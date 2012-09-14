package com.goodow.web.reader.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class TableDropDownPanel extends DropDownPanel {

  interface Binder extends UiBinder<Widget, TableDropDownPanel> {
  }

  private static Binder binder = GWT.create(Binder.class);

  private int row;

  @UiField
  HTMLPanel container;

  private HTMLPanel linePanel;

  public TableDropDownPanel(final int row) {
    this.row = row;
  }

  @Override
  public Widget addChild(final IsWidget isWidget, final ClickHandler clickHandler) {
    Widget widget = isWidget.asWidget();
    if (container.getWidgetCount() == 0 || linePanel.getWidgetCount() % row == 0) {
      linePanel = null;
      linePanel = new HTMLPanel("");
      container.add(linePanel);
    }
    linePanel.add(widget);
    if (clickHandler == null) {
      return widget;
    }
    widget.addDomHandler(clickHandler, ClickEvent.getType());
    return widget;
  }

  @Override
  public Widget initMainWidget() {
    return binder.createAndBindUi(this);
  }

}
