package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.panel.WavePanel;
import com.goodow.web.view.wave.client.panel.WavePanelResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class TagsPanel extends Composite {
  interface Binder extends UiBinder<Widget, TagsPanel> {
  }

  interface Style extends CssResource {

    String addTagPanel();
  }

  private static final Binder binder = GWT.create(Binder.class);
  @UiField
  Style style;
  @UiField
  Widget root;
  TextBox textBox;
  @UiField
  FlowPanel tagValue;
  @UiField
  Button Buttoned;
  private final WavePanel addTagPanel;

  @Inject
  TagsPanel(final WavePanel addTagPanel) {
    initWidget(binder.createAndBindUi(this));
    addStyleName(WavePanelResources.css().waveFooter());

    this.addTagPanel = addTagPanel;
    addTagPanel.addStyleName(style.addTagPanel());
    addTagPanel.getWaveTitle().setText("999");
    textBox = new TextBox();
    textBox.getElement().removeClassName("gwt-TextBox");
    addTagPanel.setWaveContent(textBox);

    Element addTags = Buttoned.getElement().getPreviousSiblingElement();
    DOM.sinkEvents((com.google.gwt.user.client.Element) addTags, Event.ONCLICK);
    DOM.setEventListener((com.google.gwt.user.client.Element) addTags, new EventListener() {
      @Override
      public void onBrowserEvent(final Event event) {
        if (event.getTypeInt() == Event.ONCLICK) {
          // popupPanel.showRelativeTo(root);
          addTagPanel.getElement().getStyle().setTop(
              getAbsoluteTop() - addTagPanel.getOffsetHeight(), Unit.PX);
          // addTagPanel.getElement().getStyle().setPadding(0, Unit.PX);
          // addTagPanel.getElement().getStyle().setPaddingBottom(2, Unit.PX);
          // addTagPanel.getElement().getStyle().setDisplay(Display.BLOCK);
          getElement().appendChild(addTagPanel.getElement());
          textBox.setFocus(true);
        }
      }
    });
    textBox.addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(final KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !textBox.equals("")) {
          Hyperlink hyperLink = new InlineHyperlink(textBox.getValue(), "#");
          tagValue.add(hyperLink);
          textBox.setText("");
          if (textBox.getValue().equals("") && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            // popupPanel.hide();
          }
        }
      }
    });

  }
}
