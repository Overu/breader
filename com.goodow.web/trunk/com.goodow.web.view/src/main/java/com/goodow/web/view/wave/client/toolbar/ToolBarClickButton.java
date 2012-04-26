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
package com.goodow.web.view.wave.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ToolBarClickButton extends Composite implements ToolBarButtonView, HasClickHandlers {
  interface ToolbarUiBinder extends UiBinder<Widget, ToolBarClickButton> {
  }

  private static ToolbarUiBinder uiBinder = GWT.create(ToolbarUiBinder.class);

  @UiField
  HTMLPanel root;
  @UiField
  Element textElement;
  @UiField
  Element visualElement;
  @UiField
  Element dropDownArrow;
  @UiField
  Element toolbarDivider;

  private ClickHandler clickHandler;
  private HandlerRegistration handlerRegistration;

  public ToolBarClickButton() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    this.clickHandler = handler;
    handlerRegistration = addDomHandler(handler, ClickEvent.getType());
    return handlerRegistration;
  }

  /**
   * set the ToolbrDivider isHidden
   */
  public void setShowDivider(final boolean showDivider) {
    toolbarDivider.getStyle().setDisplay(showDivider ? Display.BLOCK : Display.NONE);
  }

  @Override
  public void setShowDropdownArrow(final boolean showDropdownArrow) {
    dropDownArrow.getStyle().setDisplay(showDropdownArrow ? Display.BLOCK : Display.NONE);
  }

  @Override
  public void setState(final State state) {
    switch (state) {
      case ENABLED:
        root.removeStyleName(State.DISABLED.name());
        if (handlerRegistration == null) {
          addDomHandler(clickHandler, ClickEvent.getType());
        }
        break;
      case DISABLED:
        root.addStyleName(State.DISABLED.name());
        if (handlerRegistration != null) {
          handlerRegistration.removeHandler();
          handlerRegistration = null;
        }
        break;
      case INVISIBLE:
        root.getElement().getStyle().setDisplay(Display.NONE);
        break;
      default:
        break;
    }
  }

  @Override
  public void setText(final String text) {
    textElement.setInnerText(text);
    textElement.getStyle().setDisplay(Display.INLINE);
  }

  @Override
  public void setVisualElement(final Element element) {
    visualElement.appendChild(element);
  }

}
