package com.goodow.web.reader.client;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

import com.googlecode.mgwt.ui.client.MGWTStyle;

public class BooksBrowser extends FlowView {

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
  private FeedViewerList leftPanel;

  @Inject
  private SimplePanel centerPanel;

  @Override
  public void setChildWidget(final IsWidget w) {
    centerPanel.setWidget(w);
  }

  @Override
  public void setPlace(final WebPlace place) {
    super.setPlace(place);
    // leftPanel.setPlace(place);
  }

  @Override
  protected void start() {

    centerPanel.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss().fillPanelExpandChild());

    main.addStyleName(bundle.booksAppCss().main());

    // bannerPanel.addStyleName(bundle.booksAppCss().bannerPanel());

    // leftPanel.addRightWidget(centerPanel);
    // leftPanel.addStyleName(bundle.booksAppCss().placeList());

    main.add(centerPanel);
  }

}
