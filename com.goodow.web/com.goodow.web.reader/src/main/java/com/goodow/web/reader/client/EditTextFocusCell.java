package com.goodow.web.reader.client;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class EditTextFocusCell extends AbstractEditableCell<String, EditTextFocusCell.ViewData> {

  interface Template extends SafeHtmlTemplates {
    @Template("<div>{0}</div>")
    SafeHtml div(String value);

    @Template("<input style=\"width: 100px;\" type=\"text\" value=\"{0}\" tabindex=\"-1\"></input>")
    SafeHtml input(String value);

    @Template("{0}")
    SafeHtml text(String value);
  }

  static class ViewData {

    private boolean isEditing;

    private boolean isEditingAgain;

    private String original;

    private String text;

    public ViewData(final String text) {
      this.original = text;
      this.text = text;
      this.isEditing = true;
      this.isEditingAgain = false;
    }

    @Override
    public boolean equals(final Object o) {
      if (o == null) {
        return false;
      }
      ViewData vd = (ViewData) o;
      return equalsOrBothNull(original, vd.original) && equalsOrBothNull(text, vd.text)
          && isEditing == vd.isEditing && isEditingAgain == vd.isEditingAgain;
    }

    public String getOriginal() {
      return original;
    }

    public String getText() {
      return text;
    }

    @Override
    public int hashCode() {
      return original.hashCode() + text.hashCode() + Boolean.valueOf(isEditing).hashCode() * 29
          + Boolean.valueOf(isEditingAgain).hashCode();
    }

    public boolean isEditing() {
      return isEditing;
    }

    public boolean isEditingAgain() {
      return isEditingAgain;
    }

    public void setEditing(final boolean isEditing) {
      boolean wasEditing = this.isEditing;
      this.isEditing = isEditing;

      if (!wasEditing && isEditing) {
        isEditingAgain = true;
        original = text;
      }
    }

    public void setText(final String text) {
      this.text = text;
    }

    private boolean equalsOrBothNull(final Object o1, final Object o2) {
      return (o1 == null) ? o2 == null : o1.equals(o2);
    }
  }

  private static Template template;
  private boolean isChangeCell = false;

  public EditTextFocusCell() {
    super(BrowserEvents.BLUR, BrowserEvents.FOCUS, BrowserEvents.KEYUP, BrowserEvents.KEYDOWN);
    if (template == null) {
      template = GWT.create(Template.class);
    }
  }

  @Override
  public boolean isEditing(final com.google.gwt.cell.client.Cell.Context context,
      final Element parent, final String value) {
    ViewData viewData = getViewData(context.getKey());
    return viewData == null ? false : viewData.isEditing();
  }

  @Override
  public void onBrowserEvent(final com.google.gwt.cell.client.Cell.Context context,
      final Element parent, final String value, final NativeEvent event,
      final ValueUpdater<String> valueUpdater) {
    Object key = context.getKey();
    ViewData viewData = getViewData(key);
    if (viewData != null && viewData.isEditing()) {
      editEvent(context, parent, value, viewData, event, valueUpdater);
    } else {
      String type = event.getType();
      int keyCode = event.getKeyCode();
      boolean enterPressed = BrowserEvents.KEYUP.equals(type) && keyCode == KeyCodes.KEY_ENTER;
      if (type.equals(BrowserEvents.FOCUS) || enterPressed) {
        if (viewData == null) {
          viewData = new ViewData(value);
          setViewData(key, viewData);
        } else {
          viewData.setEditing(true);
        }
        edit(context, parent, value);
      }
    }
  }

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context, final String value,
      final SafeHtmlBuilder sb) {
    Object key = context.getKey();
    ViewData viewData = getViewData(key);
    if (viewData != null && !viewData.isEditing() && value != null
        && value.equals(viewData.getText())) {
      clearViewData(key);
      viewData = null;
    }

    if (viewData != null) {
      String text = viewData.getText();
      if (viewData.isEditing()) {
        sb.append(template.input(text));
        isChangeCell = true;
      } else {
        sb.append(template.text(text));
        isChangeCell = false;
      }
    } else if (value != null) {
      if (isChangeCell) {
        sb.append(template.text(value));
        isChangeCell = false;
        return;
      }
      sb.append(template.div(value));
    }
  }

  protected void edit(final Context context, final Element parent, final String value) {
    setValue(context, parent, value);
    InputElement input = getInputElement(parent);
    input.focus();
    input.select();
  }

  private void cancel(final Context context, final Element parent, final String value) {
    clearInput(getInputElement(parent));
    setValue(context, parent, value);
  }

  private native void clearInput(Element input) /*-{
                                                if (input.selectionEnd)
                                                input.selectionEnd = input.selectionStart;
                                                else if ($doc.selection)
                                                $doc.selection.clear();
                                                }-*/;

  private void commit(final Context context, final Element parent, final ViewData viewData,
      final ValueUpdater<String> valueUpdater) {
    String value = updateViewData(parent, viewData, false);
    // clearInput(getInputElement(parent));
    setValue(context, parent, viewData.getOriginal());
    if (valueUpdater != null) {
      valueUpdater.update(value);
    }
  }

  private void editEvent(final Context context, final Element parent, final String value,
      final ViewData viewData, final NativeEvent event, final ValueUpdater<String> valueUpdater) {
    String type = event.getType();
    boolean keyUp = "keyup".equals(type);
    boolean keyDown = "keydown".equals(type);
    // boolean focus = type.equals(BrowserEvents.FOCUS);
    boolean blur = type.equals(BrowserEvents.BLUR);
    if (keyUp || keyDown) {
      int keyCode = event.getKeyCode();
      if (keyUp && keyCode == KeyCodes.KEY_ENTER) {
        commit(context, parent, viewData, valueUpdater);
      } else if (keyUp && keyCode == KeyCodes.KEY_ESCAPE) {
        String originalText = viewData.getOriginal();
        if (viewData.isEditingAgain()) {
          viewData.setText(originalText);
          viewData.setEditing(false);
        } else {
          setViewData(context.getKey(), null);
        }
        cancel(context, parent, value);
      } else {
        updateViewData(parent, viewData, true);
      }
    } else if (blur) {
      EventTarget eventTarget = event.getEventTarget();
      if (Element.is(eventTarget)) {
        Element target = Element.as(eventTarget);
        target.getParentElement().removeAttribute("tabindex");
        if ("input".equals(target.getTagName().toLowerCase())) {
          commit(context, parent, viewData, valueUpdater);
        }
      }
    }
  }

  private InputElement getInputElement(final Element parent) {
    return parent.getFirstChild().<InputElement> cast();
  }

  private String updateViewData(final Element parent, final ViewData viewData,
      final boolean isEditing) {
    InputElement input = (InputElement) parent.getFirstChild();
    String value = input.getValue();
    viewData.setText(value);
    viewData.setEditing(isEditing);
    return value;
  }
}
