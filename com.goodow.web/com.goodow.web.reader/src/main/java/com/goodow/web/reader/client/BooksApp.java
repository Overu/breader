package com.goodow.web.reader.client;

import com.goodow.web.core.client.FlowView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

import com.googlecode.mgwt.ui.client.MGWTStyle;

public class BooksApp extends FlowView implements AcceptsOneWidget {

  interface Bundle extends ClientBundle {
    @Source("BooksApp.css")
    Style booksAppCss();
  }

  interface Style extends CssResource {
    String bannerPanel();

    String main();

    String placeList();
  }

  private static Bundle bundle;

  static {
    bundle = GWT.create(Bundle.class);
    bundle.booksAppCss().ensureInjected();
  }

  @Inject
  private FlowPanel bannerPanel;

  @Inject
  private PlaceList leftPanel;

  @Inject
  private UserInfoPanel userInfoPanel;

  // @Inject
  // private FlowPanel centerPanel;
  //
  // @Inject
  // private HeaderPanel centerHeader;

  @Inject
  private SimplePanel centerPanel;

  @Override
  public void setWidget(final IsWidget w) {
    centerPanel.setWidget(w);
  }

  @Override
  protected void start() {
    bannerPanel.add(new Label("睿泰数字发行平台"));
    bannerPanel.add(userInfoPanel);

    centerPanel.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss()
        .fillPanelExpandChild());

    leftPanel.addRightWidget(centerPanel);

    main.addStyleName(bundle.booksAppCss().main());
    bannerPanel.addStyleName(bundle.booksAppCss().bannerPanel());
    leftPanel.addStyleName(bundle.booksAppCss().placeList());

    main.add(bannerPanel);
    main.add(leftPanel);
  }

}
