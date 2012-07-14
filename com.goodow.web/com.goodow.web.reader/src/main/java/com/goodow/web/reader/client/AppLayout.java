package com.goodow.web.reader.client;

import com.goodow.web.core.client.WebView;
import com.goodow.web.reader.client.style.ReadResources;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.base.TabBarCss;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;

public class AppLayout extends WebView implements AcceptsOneWidget {

  private LayoutPanel main;

  @Inject
  private PlaceList placeList;

  @Inject
  LayoutPanel centerPanel;

  @Inject
  private SimplePanel childPanel;

  @Inject
  private HeaderPanel headerPanel;

  @Inject
  private HeaderButton newButton;

  public AppLayout() {
    main = new LayoutPanel();
    initWidget(main);
  }

  @Override
  public void setWidget(final IsWidget w) {
    childPanel.setWidget(w);
  }

  @Override
  protected void start() {
    String styleName = ReadResources.INSTANCE().categroyListCss().categorContainer();
    main.addStyleName(styleName);

    main.add(placeList);
    TabBarCss css = MGWTStyle.getTheme().getMGWTClientBundle().getTabBarCss();
    childPanel.addStyleName(css.tabPanelContainer());
    childPanel.getElement().getStyle().setPosition(Position.RELATIVE);

    centerPanel.add(headerPanel);
    newButton.setBackButton(true);
    newButton.setText("新建");
    headerPanel.setCenterWidget(newButton);
    centerPanel.add(childPanel);

    main.add(centerPanel);

  }

}
