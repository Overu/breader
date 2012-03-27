package com.goodow.web.ui.client.footer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class FooterUi extends Composite {

  interface FooterUiBinder extends UiBinder<Widget, FooterUi> {
  }

  private static FooterUiBinder uiBinder = GWT.create(FooterUiBinder.class);

  public FooterUi() {
    initWidget(uiBinder.createAndBindUi(this));
  }

}
