/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.goodow.web.reader.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.touch.TouchPanel;

import java.util.LinkedList;

public class ReadFixedHeadPanel extends Composite implements HasSelectionHandlers<Integer> {

  public class FixedButton extends TouchPanel {

    public FixedButton() {
    }

    public void setSelected(final boolean selected) {
      if (selected) {
        addStyleName(fbCss.selected());
      } else {
        removeStyleName(fbCss.selected());
      }
    }

    public void setText(final String text) {
      getElement().setInnerHTML(text);
    }

  }

  interface Binder extends UiBinder<Widget, ReadFixedHeadPanel> {
  }

  interface FixedHeadCss extends CssResource {
    String selected();
  }

  private class InternalTouchHandler implements TapHandler {

    private final FixedButton button;

    public InternalTouchHandler(final FixedButton button) {
      this.button = button;

    }

    @Override
    public void onTap(final TapEvent event) {
      setSelectedButton(getIndexForWidget(button));
    }

  }

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  HTMLPanel root;

  @UiField
  FixedHeadCss fbCss;

  private LinkedList<FixedButton> buttons;
  private LinkedList<HandlerRegistration> handlers = new LinkedList<HandlerRegistration>();

  public ReadFixedHeadPanel() {
    initWidget(binder.createAndBindUi(this));
    buttons = new LinkedList<FixedButton>();
  }

  public FixedButton addButton(final String text) {
    FixedButton fixedButton = new FixedButton();
    fixedButton.setText(text);

    if (buttons.size() == 0) {
      fixedButton.setSelected(true);
    }

    root.add(fixedButton);
    buttons.add(fixedButton);
    handlers.add(fixedButton.addTapHandler(new InternalTouchHandler(fixedButton)));
    return fixedButton;
  }

  @Override
  public HandlerRegistration addSelectionHandler(final SelectionHandler<Integer> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  public void setSelectedButton(final int index) {
    setSelectedButton(index, false);
  }

  public void setSelectedButton(final int index, final boolean suppressEvent) {
    if (index < 0) {
      throw new IllegalArgumentException("invalud index");
    }

    if (index >= buttons.size()) {
      throw new IllegalArgumentException("invalud index");
    }
    int count = 0;
    for (FixedButton button : buttons) {
      if (count == index) {
        button.setSelected(true);
      } else {
        button.setSelected(false);
      }
      count++;
    }
    if (!suppressEvent) {
      SelectionEvent.fire(this, Integer.valueOf(index));
    }
  }

  private int getIndexForWidget(final FixedButton w) {
    return buttons.indexOf(w);
  }
}
