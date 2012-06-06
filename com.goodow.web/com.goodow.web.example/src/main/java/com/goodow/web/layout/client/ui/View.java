package com.goodow.web.layout.client.ui;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class View extends Composite implements AcceptsOneWidget {

  interface ShellBinder extends UiBinder<Widget, View> {
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  private static ShellBinder uiBinder = GWT.create(ShellBinder.class);

  @UiField
  HTMLPanel titleBar;

  @UiField
  HTMLPanel toolBar;

  @UiField
  HTMLPanel container;

  private final WidgetRegistry registry;

  @Inject
  public View(WidgetRegistry registry) {
    this.registry = registry;
    Widget widget = uiBinder.createAndBindUi(this);
    initWidget(widget);
  }

  public void add(View view) {
    container.add(view);
  }

  public void setLabel(String label) {
    titleBar.add(new HTML(label));
  }

  @Override
  public void setWidget(IsWidget w) {
    container.clear();
    container.add(w);
  }

  public void setWidgetId(String widgetId) {
    registry.showWidget(this, widgetId);
  }

  @UiHandler("closeBtn")
  void handleCloseHide(ClickEvent e) {
    // Window.alert("Close");
    setVisible(false);
  }

}
