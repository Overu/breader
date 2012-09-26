package com.goodow.web.reader.client;

import com.goodow.web.reader.client.ColumnSortEvent.Sort;
import com.goodow.web.reader.client.editgrid.Function;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import java.util.HashMap;
import java.util.Map;

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

  private String curHeader;
  private Map<String, ColumnEntity<?>> map;

  @Inject
  public ColumnSortDropDownPanel(final PopupContainer pp) {
    addElmHandle(ascElm, new Function() {
      @Override
      public boolean f(final Event event) {
        asc();
        return true;
      }
    });

    addElmHandle(dscElm, new Function() {
      @Override
      public boolean f(final Event event) {
        dsc();
        return true;
      }
    });
    com.google.gwt.user.client.Element cast =
        columnContainer.<com.google.gwt.user.client.Element> cast();
    pp.ignore(cast);
  }

  @Override
  public Widget addChild(final IsWidget isWidget, final ClickHandler clickHandler) {
    return null;
  }

  public void addColumn(final String header, final ColumnEntity<?> columnEntity) {
    if (map == null) {
      map = new HashMap<String, ColumnEntity<?>>();
    }
    if (map.containsKey(header)) {
      return;
    }
    map.put(header, columnEntity);
    com.google.gwt.user.client.Element div = DOM.createDiv();
    com.google.gwt.user.client.Element headerDiv = DOM.createDiv();
    final InputElement inputCheck =
        DOM.createInputCheck().<com.google.gwt.dom.client.InputElement> cast();
    headerDiv.setInnerText(header);
    inputCheck.setChecked(true);
    div.appendChild(inputCheck);
    div.appendChild(headerDiv);
    columnContainer.appendChild(div);
    addElmHandle(inputCheck, new Function() {
      @Override
      public boolean f(final Event event) {
        ColumnVisiableEvent
            .fire(ColumnSortDropDownPanel.this, columnEntity, inputCheck.isChecked());
        return true;
      }
    });
  }

  public HandlerRegistration addColumnSortHandler(final ColumnSortEvent.Handler handler) {
    return this.addHandler(handler, ColumnSortEvent.TYPE);
  }

  public HandlerRegistration addColumnVisiableHandle(final ColumnVisiableEvent.Handle handler) {
    return this.addHandler(handler, ColumnVisiableEvent.TYPE);
  }

  public void addElmHandle(final Element elm, final Function f) {
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

  public String getCurHeader() {
    return curHeader;
  }

  @Override
  public Widget initMainWidget() {
    return binder.createAndBindUi(this);
  }

  public void setCurHeader(final String curHeader) {
    this.curHeader = curHeader;
  }

  private void asc() {
    sort(Sort.ASC);
  }

  private void dsc() {
    sort(Sort.DSC);
  }

  private void sort(final Sort sort) {
    ColumnSortEvent.fire(this, getCurHeader(), sort);
  }
}
