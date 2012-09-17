package com.goodow.web.reader.client;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.core.shared.FeedViewer;
import com.goodow.web.core.shared.HomePlace;
import com.goodow.web.core.shared.WebPlace;
import com.goodow.web.core.shared.WebPlaceManager;
import com.goodow.web.reader.client.BookHeadPanel.LocationPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

public class ReaderApp extends FlowView {

  @Inject
  private BookHeadPanel bannerPanel;

  @Inject
  private TopBarButton booksButton;

  @Inject
  private TopBarButton magazinesButton;

  @Inject
  private TopBarButton topBarButton;

  @Inject
  private TopBarButton topBarButton2;

  @Inject
  private TopBarButton topBarButton3;

  @Inject
  private SimplePanel centerPanel;

  @Inject
  @HomePlace
  WebPlace homePlace;

  @Inject
  WebPlaceManager placeManager;

  @Override
  public void setChildWidget(final IsWidget w) {
    centerPanel.setWidget(w);
  }

  @Override
  protected void start() {
    DropDownPanel pci = new ListDropDownPanel();
    pci.addChild(new Label("我是所有者"), null);
    pci.addChild(new Label("我是成员"), null);
    pci.addChild(new Label("我在关注"), null);
    pci.addChild(new Label("我被邀请"), null);
    pci.addChild(new Label("公共社区"), null);

    DropDownPanel pci1 = new TableDropDownPanel(3);
    pci1.addChild(new Label("文件"), null);
    pci1.addChild(new Label("与我共享"), null);
    pci1.addChild(new Label("置顶文件夹"), null);
    pci1.addChild(new Label("Wiki"), null);
    pci1.addChild(new Label("我是所有者"), null);
    pci1.addChild(new Label("公共Wiki"), null);

    DropDownPanel pci2 = new ListDropDownPanel();
    Image image = new Image(BooksBrowser.bundle.userFace());
    pci2.addChild(image, null);
    pci2.addChild(new Label("设置"), null);
    pci2.addChild(new Label("注销"), null);

    booksButton.setTitle("图书");
    booksButton.addDomHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        placeManager.goTo("/books", FeedViewer.MY_CONTENT);
      }
    }, ClickEvent.getType());

    magazinesButton.setTitle("杂志");
    magazinesButton.addDomHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        placeManager.goTo("/magazines");
      }
    }, ClickEvent.getType());

    topBarButton.setPopupComponent(pci);
    topBarButton.setTitle("社区");
    topBarButton2.setPopupComponent(pci2);
    topBarButton2.setTitle("登录");
    topBarButton3.setTitle("客户端");
    bannerPanel.addTopBarButton(booksButton, LocationPanel.Left);
    bannerPanel.addTopBarButton(magazinesButton, LocationPanel.Left);
    bannerPanel.addTopBarButton(topBarButton, LocationPanel.Left);
    bannerPanel.addTopBarButton(topBarButton2, LocationPanel.Right);
    bannerPanel.addTopBarButton(topBarButton3, LocationPanel.Right);

    centerPanel.getElement().setId("main-center");
    centerPanel.addStyleName(BooksBrowser.bundle.booksAppCss().centerpanel());

    main.addStyleName(BooksBrowser.bundle.booksAppCss().main());

    main.add(bannerPanel);
    main.add(centerPanel);
  }
}
