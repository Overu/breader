package com.goodow.web.core.client;

import com.google.gwt.user.client.ui.RichTextArea;
import com.google.inject.Inject;

public class RichTextField extends FormField<String> {

  @Inject
  RichTextArea textArea;

  @Override
  public String getValue() {
    return textArea.getHTML();
  }

  @Override
  public void setValue(final String value) {
    this.textArea.setText(value);
  }

  @Override
  protected void start() {
    super.start();
    main.add(textArea);
  }

}
