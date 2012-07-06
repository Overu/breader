package com.goodow.web.core.client;

import com.goodow.web.core.shared.MyPlace;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

import java.util.logging.Logger;

public class MyActivity extends MGWTAbstractActivity {

  private static final Logger logger = Logger.getLogger(CoreClientModule.class.getName());

  @Inject
  PlaceController placeController;

  @Inject
  private UIRegistry registry;

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    MyPlace place = (MyPlace) placeController.getWhere();
    registry.showWidget(panel, place.getUri());

  }

}
