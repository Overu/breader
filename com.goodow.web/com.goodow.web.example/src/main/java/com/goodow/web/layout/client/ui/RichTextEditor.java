package com.goodow.web.layout.client.ui;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class RichTextEditor extends Composite {

  interface ShellBinder extends UiBinder<Widget, RichTextEditor> {
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  private static ShellBinder uiBinder = GWT.create(ShellBinder.class);

  @UiField
  RichTextArea textArea;

  private final WidgetRegistry registry;

  @Inject
  public RichTextEditor(WidgetRegistry registry) {
    this.registry = registry;
    Widget widget = uiBinder.createAndBindUi(this);
    initWidget(widget);
  }
}
