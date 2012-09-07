package com.goodow.web.reader.client.editgrid;

import com.goodow.web.reader.client.droppable.Events;
import com.goodow.web.reader.client.droppable.SimpleQuery;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ContentCtrlsItem extends Composite {

  interface Binder extends UiBinder<Widget, ContentCtrlsItem> {
  }

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  DivElement clearElm;
  @UiField
  DivElement editBookElm;
  @UiField
  DivElement floatLeftElm;
  @UiField
  DivElement floatRightElm;
  @UiField
  DivElement dockingTopElm;
  @UiField
  DivElement dockingLeftElm;
  @UiField
  DivElement noneElm;
  @UiField
  DivElement hsplitElm;
  @UiField
  DivElement vsplitElm;

  public ContentCtrlsItem() {
    initWidget(binder.createAndBindUi(this));
  }

  public void addClearElmHandle(final Function f) {
    SimpleQuery.q(clearElm).as(Events.Events).bind(Event.ONCLICK, (Object) null, f);
  }

  public void addDockingLeftElmHandle(final Function f) {
    SimpleQuery.q(dockingLeftElm).as(Events.Events).bind(Event.ONCLICK, (Object) null, f);
  }

  public void addDockingTopElmHandle(final Function f) {
    SimpleQuery.q(dockingTopElm).as(Events.Events).bind(Event.ONCLICK, (Object) null, f);
  }

  public void addEditBookElmHandle(final Function f) {
    SimpleQuery.q(editBookElm).as(Events.Events).bind(Event.ONCLICK, (Object) null, f);
  }

  public void addFloatLeftElmHandle(final Function f) {
    SimpleQuery.q(floatLeftElm).as(Events.Events).bind(Event.ONCLICK, (Object) null, f);
  }

  public void addFloatRightElmHandle(final Function f) {
    SimpleQuery.q(floatRightElm).as(Events.Events).bind(Event.ONCLICK, (Object) null, f);
  }

  public void addHsplitElmHandle(final Function f) {
    SimpleQuery.q(hsplitElm).as(Events.Events).bind(Event.ONCLICK, (Object) null, f);
  }

  public void addNoneElmHandle(final Function f) {
    SimpleQuery.q(noneElm).as(Events.Events).bind(Event.ONCLICK, (Object) null, f);
  }

  public void addVsplitElmHandle(final Function f) {
    SimpleQuery.q(vsplitElm).as(Events.Events).bind(Event.ONCLICK, (Object) null, f);
  }

}
