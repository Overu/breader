package com.goodow.web.core.client;

import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

import java.util.logging.Logger;

public class WebActivity extends MGWTAbstractActivity {

  private static final Logger logger = Logger.getLogger(WebActivity.class.getName());

  @Inject
  PlaceController placeController;

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    WebPlace place = (WebPlace) placeController.getWhere();
    logger.info("Start " + place);
    place.render(panel);
  }

}
