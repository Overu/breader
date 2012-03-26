package com.goodow.web.view.wave.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

public class ToolbarClickButton {
  private final Element element = DOM.createDiv();

  public Element getElement() {
    return element;
  }

  /**
   * Sets the listener to click events.
   */
  public void setListener(final EventListener listener) {
    DOM.sinkEvents(element, Event.ONCLICK);
    DOM.setEventListener(element, listener);
  }

  public void setShowDropdownArrow(final boolean showDropdown) {
  }

  public void setText(final String text) {
    Element div = DOM.createDiv();
    div.setInnerText(text);
    element.appendChild(div);
  }

  /**
   * Sets the "visual element" of a button, for example an icon or unread count.
   */
  public void setVisualElement(final Element element) {
    element.insertFirst(element);
  }
}
