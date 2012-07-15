package com.goodow.web.core.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import java.util.logging.Logger;

public class RichTextEditor extends Composite {

  interface ShellBinder extends UiBinder<Widget, RichTextEditor> {
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  private static ShellBinder uiBinder = GWT.create(ShellBinder.class);

  @UiField
  RichTextArea textArea;

  @Inject
  public RichTextEditor() {
    Widget widget = uiBinder.createAndBindUi(this);
    initWidget(widget);
  }

  public String getHTML() {
    return textArea.getHTML();
  }
}
