package com.goodow.web.reader.client;

import com.goodow.web.reader.client.editgrid.Function;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class ColumnSortDropDownPanel extends DropDownPanel {

  interface Binder extends UiBinder<Widget, ColumnSortDropDownPanel> {
  }

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  DivElement ascElm;
  @UiField
  DivElement dscElm;
  @UiField
  DivElement columnContainer;

  public ColumnSortDropDownPanel() {

  }

  public void addAscElmHandle(final Function f) {
    baseHandle(ascElm, f);
  }

  @Override
  public Widget addChild(final IsWidget isWidget, final ClickHandler clickHandler) {
    return null;
  }

  public void addDscElmHandle(final Function f) {
    baseHandle(dscElm, f);
  }

  public void baseHandle(final Element elm, final Function f) {
    DOM.sinkEvents((com.google.gwt.user.client.Element) elm, Event.ONCLICK);
    DOM.setEventListener((com.google.gwt.user.client.Element) elm, new EventListener() {

      @Override
      public void onBrowserEvent(final Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK) {
          if (f == null) {
            return;
          }
          f.f(event);
        }
      }
    });
  }

  @Override
  public Widget initMainWidget() {
    return binder.createAndBindUi(this);
  }

}
