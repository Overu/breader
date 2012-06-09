package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.mvp.shared.rpc.BaseEditor;
import com.goodow.web.mvp.shared.tree.rpc.BaseContext;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

import com.retech.reader.web.shared.proxy.IssueProxy;


@Singleton
public class TestEditor extends BaseEditor<IssueProxy> {
  interface Binder extends UiBinder<Widget, TestEditor> {
  }
  interface Driver extends RequestFactoryEditorDriver<IssueProxy, TestEditor> {
  }

  private static Binder binder = GWT.create(Binder.class);
  private static Driver driver = GWT.create(Driver.class);

  @UiField
  HTMLPanel scrollBar;

  @UiField
  DivElement textDiv;

  @UiField
  HTMLPanel showDiv;

  public TestEditor() {
    super.initEditor();
  }

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    scrollBar.addDomHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        int textDivClientHeight = textDiv.getClientHeight();
        int showDivClientHeight = showDiv.getElement().getClientHeight();
        int scrollHeight = textDiv.getScrollHeight();
        int showTop = textDivClientHeight - showDivClientHeight;
        if (event.getY() <= 19) {
          int top = textDiv.getScrollTop() - textDiv.getClientHeight();
          textDiv.setScrollTop(top);
          showDiv.getElement().getStyle().setTop(
              ((double) showTop / (scrollHeight - textDivClientHeight)) * textDiv.getScrollTop(),
              Unit.PX);
        } else if (event.getY() >= 52) {
          int top = textDiv.getScrollTop() + textDiv.getClientHeight();
          textDiv.setScrollTop(top);
          showDiv.getElement().getStyle().setTop(
              ((double) showTop / (scrollHeight - textDivClientHeight)) * textDiv.getScrollTop(),
              Unit.PX);
        }
      }
    }, ClickEvent.getType());
  }

  @Override
  protected RequestFactoryEditorDriver<IssueProxy, TestEditor> provideEditorDriver() {
    return driver;
  }

  @Override
  protected BaseContext provideRequestContext() {
    return null;
  }

  @Override
  protected UiBinder provideUiBinder() {
    return binder;
  }

}
