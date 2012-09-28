package com.goodow.web.reader.client;

import com.goodow.web.reader.client.editgrid.Function;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Map.Entry;

@Singleton
public class BooksViewBrowser extends Composite implements ColumnVisiableEvent.Handle {

  interface Binder extends UiBinder<Widget, BooksViewBrowser> {
  }

  interface MyStyle extends CssResource {
    String checkrootblack();

    String toolbaron();
  }

  private static Binder binder = GWT.create(Binder.class);
  private static final String ENABLE = "enable";

  @UiField
  protected static DivElement commitElm;
  @UiField
  protected static DivElement deleteElm;
  @UiField
  protected static DivElement refreshElm;

  public static void diable() {
    commitElm.removeAttribute(ENABLE);
    deleteElm.removeAttribute(ENABLE);
    refreshElm.removeAttribute(ENABLE);
  }

  public static void enable() {
    commitElm.setAttribute(ENABLE, "");
    deleteElm.setAttribute(ENABLE, "");
    refreshElm.setAttribute(ENABLE, "");
  }

  @UiField
  MyStyle style;
  @UiField
  SimplePanel container;
  @UiField
  DivElement customElm;
  @UiField
  DivElement listElm;
  @UiField
  DivElement customButton;

  @UiField
  DivElement cancelButton;
  @UiField
  DivElement checkContainer;

  @Inject
  private DataGridView dataGridView;

  @Inject
  private BookListView bookListView;

  private BaseViewBrowser currentView;

  @Inject
  public BooksViewBrowser() {
    initWidget(binder.createAndBindUi(this));
    addHandler(this, ColumnVisiableEvent.TYPE);
    addListenerHandler(customElm, new Function() {
      @Override
      public boolean f(final Event event) {
        if (setCurView(customElm, dataGridView)) {
          return true;
        }
        return true;
      }
    });
    addListenerHandler(listElm, new Function() {
      @Override
      public boolean f(final Event event) {
        if (setCurView(listElm, bookListView)) {
          return true;
        }
        return true;
      }
    });
    addListenerHandler(customButton, new Function() {
      @Override
      public boolean f(final Event event) {
        putColumn();
        checkContainer.getParentElement().addClassName(style.checkrootblack());
        return true;
      }
    });
    addListenerHandler(cancelButton, new Function() {
      @Override
      public boolean f(final Event event) {
        checkContainer.getParentElement().removeClassName(style.checkrootblack());
        return true;
      }
    });
    new Timer() {
      @Override
      public void run() {
        start();
      }
    }.schedule(1);
  }

  @Override
  public void onColumnVisiable(final ColumnVisiableEvent columnVisiableEvent) {
    boolean checked = columnVisiableEvent.isChecked();
    ColumnEntity<Book> columnEntity = (ColumnEntity<Book>) columnVisiableEvent.getColumnEntity();
    if (currentView != dataGridView) {
      setCurView(customElm, dataGridView);
    }
    DataGrid<Book> cellView = currentView.<DataGrid<Book>> getCellView();
    if (checked) {
      cellView.setColumnWidth(columnEntity.getColumn(), columnEntity.getWidth(), Unit.PX);
    } else {
      cellView.setColumnWidth(columnEntity.getColumn(), 0, Unit.PX);
    }
  }

  @Override
  protected void onLoad() {
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

  private void putColumn() {
    if (checkContainer.getChildCount() != 0) {
      return;
    }
    for (Entry<String, ColumnEntity<?>> entry : dataGridView.columns.entrySet()) {
      String header = entry.getKey();
      final ColumnEntity<?> columnEntity = entry.getValue();
      com.google.gwt.user.client.Element div = DOM.createDiv();
      com.google.gwt.user.client.Element headerDiv = DOM.createDiv();
      final InputElement inputCheck =
          DOM.createInputCheck().<com.google.gwt.dom.client.InputElement> cast();
      headerDiv.setInnerText(header);
      inputCheck.setChecked(true);
      div.appendChild(inputCheck);
      div.appendChild(headerDiv);
      checkContainer.appendChild(div);
      addListenerHandler(inputCheck, new Function() {
        @Override
        public boolean f(final Event event) {
          ColumnVisiableEvent.fire(BooksViewBrowser.this, columnEntity, inputCheck.isChecked());
          return true;
        }
      });
    }
  }

  private boolean setCurView(final Element elm, final BaseViewBrowser view) {
    if (elm.getClassName().contains(style.toolbaron()) && currentView == view) {
      return true;
    }
    currentView = view;
    container.setWidget(currentView.getView());
    elm.addClassName(style.toolbaron());
    Element nextSiblingElement = elm.getNextSiblingElement();
    if (nextSiblingElement == null) {
      elm.getPreviousSiblingElement().removeClassName(style.toolbaron());
      return false;
    }
    nextSiblingElement.removeClassName(style.toolbaron());
    return false;
  }

  private void start() {
    setCurView(customElm, dataGridView);
  }

}
