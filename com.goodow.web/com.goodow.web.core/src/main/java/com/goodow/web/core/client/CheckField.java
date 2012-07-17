package com.goodow.web.core.client;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.inject.Inject;

public class CheckField extends FormField<Boolean> {

  @Inject
  protected CheckBox textBox;

  @Override
  public Boolean getValue() {
    return textBox.getValue();
  }

  @Override
  public void setValue(final Boolean value) {
    this.textBox.setValue(value);
  }

  @Override
  protected void start() {
    super.start();
    main.add(textBox);
  }

}
