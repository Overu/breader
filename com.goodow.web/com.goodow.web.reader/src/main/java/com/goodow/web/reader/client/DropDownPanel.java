package com.goodow.web.reader.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public abstract class DropDownPanel extends Composite {

  interface Bundle extends ClientBundle {

    @Source("DropDownPanel.css")
    Style css();
  }

  interface Style extends CssResource {

    String root();

  }

  private static Bundle bundle;

  static {
    bundle = GWT.create(Bundle.class);
    bundle.css().ensureInjected();
  }

  public DropDownPanel() {
    Widget widget = initMainWidget();
    initWidget(widget);
    widget.addStyleName(bundle.css().root());
    // main.addStyleName(bundle.css().root());
  }

  public abstract Widget addChild(IsWidget isWidget, ClickHandler clickHandler);

  public abstract Widget initMainWidget();

}
