package com.goodow.web.view.wave.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ToolbarClickButton extends Composite implements HasClickHandlers {
  /**
   * The possible display states of a toolbar button.
   */
  public static enum State {
    ENABLED, DISABLED, INVISIBLE
  }
  interface Style extends CssResource {

    String waveToolbarButtonDisabled();
  }
  interface ToolbarUiBinder extends UiBinder<Widget, ToolbarClickButton> {
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
  Element overlay;
  @UiField
  Style style;
  @UiField
  Element toolbarDivider;

  public ToolbarClickButton() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return root.addDomHandler(handler, ClickEvent.getType());
  }

  /**
   * set the ToolbrDivider isHidden
   */
  public void setShowDivider(final boolean showDivider) {
    if (showDivider) {
      toolbarDivider.getStyle().setDisplay(Display.BLOCK);
    } else {
      toolbarDivider.getStyle().setDisplay(Display.NONE);
    }
  }

  /**
   * Set DropdownArrow is displayed
   */
  public void setShowDropdownArrow(final boolean showDropdownArrow) {
    if (showDropdownArrow) {
      dropDownArrow.getStyle().setDisplay(Display.BLOCK);
    } else {
      dropDownArrow.getStyle().setDisplay(Display.NONE);
    }
  }

  /**
   * Sets the display state,
   */
  public void setState(final State state) {
    switch (state) {
      case ENABLED:
        root.removeStyleName(style.waveToolbarButtonDisabled());
        break;
      case DISABLED:
        root.addStyleName(style.waveToolbarButtonDisabled());
        break;
      case INVISIBLE:
        root.getElement().getStyle().setDisplay(Display.NONE);
        break;
      default:
        break;
    }
  }

  public void setText(final String text) {
    textElement.setInnerText(text);
    textElement.getStyle().setDisplay(Display.INLINE);
  }

  /**
   * Sets the "visual element" of a button, for example an icon or unread count.
   */
  public void setVisualElement(final Element element) {
    visualElement.appendChild(element);
  }

}
