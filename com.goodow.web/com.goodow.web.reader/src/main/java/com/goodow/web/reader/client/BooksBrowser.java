package com.goodow.web.reader.client;

import com.goodow.web.core.client.FlowView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

import com.googlecode.mgwt.ui.client.MGWTStyle;

public class BooksBrowser extends FlowView implements AcceptsOneWidget {

  public interface Style extends CssResource {

    String centerpanel();

    String main();

    String placeList();
  }

  interface Bundle extends ClientBundle {
    @Source("BooksApp.css")
    Style booksAppCss();

    ImageResource userFace();
  }

  public static Bundle bundle;

  static {
    bundle = GWT.create(Bundle.class);
    bundle.booksAppCss().ensureInjected();
  }

  @Inject
  private PlaceList leftPanel;

  @Inject
  private SimplePanel centerPanel;

  @Override
  public void setWidget(final IsWidget w) {
    centerPanel.setWidget(w);
  }

  @Override
  protected void start() {

    centerPanel.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss()
        .fillPanelExpandChild());

    leftPanel.addRightWidget(centerPanel);

    main.addStyleName(bundle.booksAppCss().main());
    // bannerPanel.addStyleName(bundle.booksAppCss().bannerPanel());
    leftPanel.addStyleName(bundle.booksAppCss().placeList());

    main.add(leftPanel);
  }

}
