package com.goodow.web.view.wave.client.toolbar;

import com.goodow.wave.client.widget.toolbar.buttons.ToolBarButtonView;

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
import com.google.gwt.user.client.ui.Widget;

public class VerticalToolbarButtonWidget extends Composite implements ToolBarButtonView,
    HasClickHandlers {
  interface Style extends CssResource {
    String waveToolbarButtonDisabled();
  }
  interface ToolbarUiBinder extends UiBinder<Widget, VerticalToolbarButtonWidget> {
  }

  private static ToolbarUiBinder uiBinder = GWT.create(ToolbarUiBinder.class);
  @UiField
  Style style;
  @UiField
  Element content;
  @UiField
  Element textElement;
  @UiField
  Element visualElement;
  @UiField
  Element divider;
  @UiField
  Element dropdownArrow;
  private ClickHandler listener;

  private HandlerRegistration handlerRegistration;

  public VerticalToolbarButtonWidget() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    this.listener = handler;
    handlerRegistration = addDomHandler(handler, ClickEvent.getType());
    return handlerRegistration;
  }

  /**
   * set the ToolbrDivider isHidden
   */
  public void setShowDivider(final boolean showDivider) {
    divider.getStyle().setDisplay(showDivider ? Display.BLOCK : Display.NONE);
  }

  @Override
  public void setShowDropdownArrow(final boolean showDropdownArrow) {
    dropdownArrow.getStyle().setDisplay(showDropdownArrow ? Display.INLINE_BLOCK : Display.NONE);
  }

  @Override
  public void setState(final State state) {
    switch (state) {
      case ENABLED:
        content.removeClassName(style.waveToolbarButtonDisabled());
        if (handlerRegistration == null) {
          addDomHandler(listener, ClickEvent.getType());
        }
        break;
      case DISABLED:
        content.addClassName(style.waveToolbarButtonDisabled());
        if (handlerRegistration != null) {
          handlerRegistration.removeHandler();
          handlerRegistration = null;
        }
        break;
      case INVISIBLE:
        content.getStyle().setDisplay(Display.NONE);
        break;
      default:
        break;
    }
  }

  @Override
  public void setText(final String text) {
    textElement.setInnerText(text);
  }

  @Override
  public void setVisualElement(final Element element) {
    visualElement.appendChild(element);
  }

  // @UiHandler("content")
  // void handleButtonClicked(final ClickEvent e) {
  // if (listener != null) {
  // listener.onClick(e);
  // }
  // }
}
