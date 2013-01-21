/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.goodow.web.reader.client;

import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class TextAreaCell extends AbstractInputCell<String, TextAreaCell.ViewData> {

  public static class ViewData {

    private String lastValue;

    private String curValue;

    public ViewData(final String value) {
      this.lastValue = value;
      this.curValue = value;
    }

    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof ViewData)) {
        return false;
      }
      ViewData vd = (ViewData) other;
      return equalsOrNull(lastValue, vd.lastValue) && equalsOrNull(curValue, vd.curValue);
    }

    public String getCurrentValue() {
      return curValue;
    }

    public String getLastValue() {
      return lastValue;
    }

    @Override
    public int hashCode() {
      return (lastValue + "_*!@HASH_SEPARATOR@!*_" + curValue).hashCode();
    }

    protected void setCurrentValue(final String curValue) {
      this.curValue = curValue;
    }

    protected void setLastValue(final String lastValue) {
      this.lastValue = lastValue;
    }

    private boolean equalsOrNull(final Object a, final Object b) {
      return (a != null) ? a.equals(b) : ((b == null) ? true : false);
    }
  }

  interface Template extends SafeHtmlTemplates {
    @Template("<textarea rows=\"3\" dir style=\"resize: none;margin: 2px; width: 130px;height: 50px;\">{0}</textarea>")
    SafeHtml input(String value);
  }

  private static Template template;

  public TextAreaCell() {
    super("change", "keyup");
    if (template == null) {
      template = GWT.create(Template.class);
    }
  }

  @Override
  public void onBrowserEvent(final Context context, final Element parent, final String value, final NativeEvent event,
      final ValueUpdater<String> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);

    // Ignore events that don't target the input.
    InputElement input = getInputElement(parent);
    Element target = event.getEventTarget().cast();
    if (!input.isOrHasChild(target)) {
      return;
    }

    String eventType = event.getType();
    Object key = context.getKey();
    if ("change".equals(eventType)) {
      finishEditing(parent, value, key, valueUpdater);
    } else if ("keyup".equals(eventType)) {
      // Record keys as they are typed.
      ViewData vd = getViewData(key);
      if (vd == null) {
        vd = new ViewData(value);
        setViewData(key, vd);
      }
      vd.setCurrentValue(input.getValue());
    }
  }

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context, final String value, final SafeHtmlBuilder sb) {
    Object key = context.getKey();
    ViewData viewData = getViewData(key);
    if (viewData != null && viewData.getCurrentValue().equals(value)) {
      clearViewData(key);
      viewData = null;
    }

    String s = (viewData != null) ? viewData.getCurrentValue() : value;
    if (s != null) {
      sb.append(template.input(s));
    } else {
      sb.appendHtmlConstant("<textarea  rows=\"3\" dir style=\"resize: none;margin: 2px; width: 130px;height: 50px;\"></textarea>");
    }
  }

  @Override
  protected void finishEditing(final Element parent, final String value, final Object key, final ValueUpdater<String> valueUpdater) {
    String newValue = getInputElement(parent).getValue();

    // Get the view data.
    ViewData vd = getViewData(key);
    if (vd == null) {
      vd = new ViewData(value);
      setViewData(key, vd);
    }
    vd.setCurrentValue(newValue);

    // Fire the value updater if the value has changed.
    if (valueUpdater != null && !vd.getCurrentValue().equals(vd.getLastValue())) {
      vd.setLastValue(newValue);
      valueUpdater.update(newValue);
    }

    // Blur the element.
    super.finishEditing(parent, newValue, key, valueUpdater);
  }

  @Override
  protected InputElement getInputElement(final Element parent) {
    return super.getInputElement(parent).<InputElement> cast();
  }

}
