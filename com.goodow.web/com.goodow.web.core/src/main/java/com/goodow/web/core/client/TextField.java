package com.goodow.web.core.client;

import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;

public class TextField extends FormField<String> {

  @Inject
  protected TextBox textBox;

  @Override
  public String getValue() {
    return textBox.getValue();
  }

  @Override
  protected void start() {
    super.start();
    main.add(textBox);
  }

}
