package com.retech.reader.web.client.labs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TopBar extends Composite {

  interface TopBarUiBinder extends UiBinder<Widget, TopBar> {
  }

  private static TopBarUiBinder uiBinder = GWT.create(TopBarUiBinder.class);

  public TopBar() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiHandler("labs")
  void handleClick(final ClickEvent e) {
    Window.Location.assign(Window.Location.createUrlBuilder().setPath("labs.html").buildString());
  }

}
