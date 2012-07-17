package com.goodow.web.core.client;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

public abstract class FormField<T> extends FlowView {

  @Inject
  protected Label label;

  @Inject
  protected Label message;

  public abstract T getValue();

  public void setLabel(final String text) {
    label.setText(text);
  }

  public abstract void setValue(T value);

  @Override
  protected void start() {
    main.add(label);
    main.add(message);
  }
}
