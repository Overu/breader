package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.contact.UserStatusResources;
import com.goodow.web.view.wave.client.panel.WavePanelResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class UserStatus extends Composite {
  interface Binder extends UiBinder<Widget, UserStatus> {
  }
  interface Resources extends ClientBundle {
    ImageResource UserStatusUnknown();
  }

  private static Resources res = GWT.create(Resources.class);
  private Binder binder = GWT.create(Binder.class);
  @UiField
  FlowPanel users;

  public UserStatus() {
    initWidget(binder.createAndBindUi(this));
    addUser(UserStatusResources.css().waveUserAvailable(), res.UserStatusUnknown());
    addUser(UserStatusResources.css().waveUserIdle(), res.UserStatusUnknown());
    addUser(UserStatusResources.css().waveUserInvisible(), res.UserStatusUnknown());
    addUser(UserStatusResources.css().waveUserGroup(), res.UserStatusUnknown());
    addStyleName(WavePanelResources.css().waveHeader());
  }

  private void addUser(final String userStatusCss, final ImageResource imageResource) {
    SimplePanel simplePanel = new SimplePanel();
    simplePanel.add(AbstractImagePrototype.create(imageResource).createImage());
    simplePanel.addStyleName(userStatusCss);
    simplePanel.addStyleName(UserStatusResources.css().waveUser());
    simplePanel.getElement().getStyle().setProperty("padding", "0 0 7px 2px");
    simplePanel.getElement().getFirstChildElement().getStyle().setWidth(40, Unit.PX);
    simplePanel.getElement().getFirstChildElement().getStyle().setHeight(40, Unit.PX);
    users.add(simplePanel);
  }
}
