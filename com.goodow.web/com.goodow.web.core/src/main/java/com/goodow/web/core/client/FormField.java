package com.goodow.web.core.client;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

public abstract class FormField<T> extends FlowView implements TakesValue<T>,
    IsEditor<TakesValueEditor<T>>, Editor<T> {

  @Ignore
  protected TakesValueEditor<T> editor;

  @Inject
  @Ignore
  protected Label label;

  @Inject
  @Ignore
  protected Label message;

  @Override
  public TakesValueEditor<T> asEditor() {
    if (editor == null) {
      editor = TakesValueEditor.of(this);
    }
    return editor;
  }

  public void setLabel(final String text) {
    label.setText(text);
  }

  @Override
  protected void start() {
    main.add(label);
    main.add(message);
  }
}
