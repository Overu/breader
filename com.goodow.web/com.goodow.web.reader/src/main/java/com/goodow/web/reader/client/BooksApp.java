package com.goodow.web.reader.client;

import com.goodow.web.core.client.WebView;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;

public class BooksApp extends WebView implements AcceptsOneWidget {

  @Inject
  private FlowPanel bannerPanel;

  @Inject
  private FlowPanel middlePanel;

  @Inject
  private PlaceList placeList;

  @Inject
  private FlowPanel centerPanel;

  @Inject
  private HeaderPanel centerHeader;

  @Inject
  private SimplePanel container;

  @Override
  public void setWidget(final IsWidget w) {
    container.setWidget(w);
  }

  @Override
  protected void start() {
    bannerPanel.add(new Label("睿泰数字发行平台"));
    // String styleName = ReadResources.INSTANCE().categroyListCss().categorContainer();
    // middlePanel.addStyleName(styleName);

    middlePanel.add(placeList);
    container.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss()
        .fillPanelExpandChild());
    container.getElement().getStyle().setPosition(Position.RELATIVE);

    middlePanel.add(centerPanel);
    centerPanel.add(centerHeader);
    centerPanel.add(container);

    main.add(bannerPanel);
    main.add(middlePanel);
  }

}
