package com.goodow.web.reader.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class UserInfoPanel extends Composite {

  interface Binder extends UiBinder<Widget, UserInfoPanel> {
  }

  private static Binder binder = GWT.create(Binder.class);

  public UserInfoPanel() {
    initWidget(binder.createAndBindUi(this));
  }

}
