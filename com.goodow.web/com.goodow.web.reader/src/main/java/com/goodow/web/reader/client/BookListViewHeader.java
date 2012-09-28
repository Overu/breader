package com.goodow.web.reader.client;

import com.goodow.web.reader.client.ColumnSortEvent.Sort;
import com.goodow.web.reader.client.editgrid.Function;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class BookListViewHeader extends Composite {

  interface Binder extends UiBinder<Widget, BookListViewHeader> {
  }

  interface MyStyle extends CssResource {

    String sortarrowsASC();

    String sortarrowsDSC();
  }

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  InputElement checkBoxElm;
  @UiField
  DivElement sortContainer;
  @UiField
  MyStyle style;

  private LabelElement curSortLabelElm;
  private boolean curSortAscOrDsc = false;

  public BookListViewHeader() {
    initWidget(binder.createAndBindUi(this));
  }

  public void addAllCheckHandle(final Function f) {
    addListenerHandler(checkBoxElm, f);
  }

  public HandlerRegistration addColumnSortHandler(final ColumnSortEvent.Handler handler) {
    return this.addHandler(handler, ColumnSortEvent.TYPE);
  }

  public void addSortLable(final String header) {
    final LabelElement lable = DOM.createLabel().<com.google.gwt.dom.client.LabelElement> cast();
    lable.setInnerHTML(header);
    addListenerHandler(lable, new Function() {
      @Override
      public boolean f(final Event event) {
        if (curSortLabelElm != lable) {
          curSortAscOrDsc = false;
        }
        if (curSortLabelElm != null) {
          curSortLabelElm.removeClassName(style.sortarrowsDSC());
          curSortLabelElm.removeClassName(style.sortarrowsASC());
        }
        curSortAscOrDsc = !curSortAscOrDsc;
        ColumnSortEvent
            .fire(BookListViewHeader.this, header, curSortAscOrDsc ? Sort.ASC : Sort.DSC);
        lable.addClassName(curSortAscOrDsc ? style.sortarrowsASC() : style.sortarrowsDSC());
        if (curSortLabelElm == lable) {
          return true;
        }
        curSortLabelElm = lable;
        return true;
      }
    });
    sortContainer.appendChild(lable);
  }

  @Override
  protected void onLoad() {
    curSortAscOrDsc = false;
    super.onLoad();
  }

  private void addListenerHandler(final Element elm, final Function f) {
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
}
