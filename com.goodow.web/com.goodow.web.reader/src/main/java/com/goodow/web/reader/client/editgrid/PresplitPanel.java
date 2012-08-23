package com.goodow.web.reader.client.editgrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PresplitPanel extends Composite implements HasSelectionHandlers<Integer> {

  interface Binder extends UiBinder<Widget, PresplitPanel> {
  }

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  DivElement container;

  PresplitPanel() {
    initWidget(binder.createAndBindUi(this));
    NodeList<Node> childNodes = container.getChildNodes();
    for (int i = 0; i < childNodes.getLength() - 5; i++) {
      Element as = Element.as(childNodes.getItem(i * 2 + 1));
      addSplitHandler((com.google.gwt.user.client.Element) as, i);
    }
  }

  @Override
  public HandlerRegistration addSelectionHandler(final SelectionHandler<Integer> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  private void addSplitHandler(final com.google.gwt.user.client.Element elm, final int index) {
    DOM.sinkEvents(elm, Event.ONCLICK);
    DOM.setEventListener(elm, new EventListener() {

      @Override
      public void onBrowserEvent(final Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK) {
          SelectionEvent.fire(PresplitPanel.this, index);
        }
      }
    });
  }
}
