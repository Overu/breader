package com.goodow.web.core.client;

import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

@Singleton
public class ReadActivity extends MGWTAbstractActivity {

  @Inject
  PlaceController placeController;

  @Inject
  ReadView readView;

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    WebPlace place = (WebPlace) placeController.getWhere();
    readView.getHeader().setText(place.getTitle());
    panel.setWidget(readView);
  }

}
