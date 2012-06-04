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
package com.goodow.wave.client.widget.button.icon;

import com.goodow.wave.client.widget.toolbar.buttons.ToolBarButtonView.State;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class IconButtonTemplate extends Widget implements HasClickHandlers {
  public interface Resources extends ClientBundle {
    Resources INSTANCE = GWT.create(Resources.class);

    @Source("IconButtonTemplate.css")
    Style style();
  }
  public interface Style extends CssResource {
    String iconButtonDisabled();

    String visualNumeralElement();
  }

  private ClickHandler clickHandler;
  private HandlerRegistration handlerRegistration;

  static {
    Resources.INSTANCE.style().ensureInjected();
  }

  public IconButtonTemplate() {
    setElement(DOM.createDiv());
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    this.clickHandler = handler;
    handlerRegistration = addDomHandler(handler, ClickEvent.getType());
    return handlerRegistration;
  }

  /**
   * create the waveCustomerIconButton
   */
  public void setIconElement(final Element element) {
    this.getElement().appendChild(element);
  }

  public IconButtonTemplate setIconText(final String text) {
    getElement().setInnerText(text);
    addStyleName(Resources.INSTANCE.style().visualNumeralElement());
    return this;
  }

  public void setState(final State state) {
    switch (state) {
      case ENABLED:
        this.removeStyleName(Resources.INSTANCE.style().iconButtonDisabled());
        if (handlerRegistration == null) {
          addDomHandler(clickHandler, ClickEvent.getType());
        }
        break;
      case DISABLED:
        if (handlerRegistration != null) {
          handlerRegistration.removeHandler();
          handlerRegistration = null;
        }
        this.addStyleName(Resources.INSTANCE.style().iconButtonDisabled());
        break;
    }
  }
}
