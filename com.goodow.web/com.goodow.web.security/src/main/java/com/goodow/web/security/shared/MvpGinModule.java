package com.goodow.web.security.shared;

import com.goodow.web.mvp.shared.BasePlace;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.Place;
import com.google.inject.Provides;


import java.util.logging.Logger;

public final class MvpGinModule extends AbstractGinModule {

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
  }

  @Provides
  @Auth
  Place loginPlaceProvider(final BasePlace place) {
    place.setPath(Auth.PLACE_REQUEST_AUTH);
    return place;
  }

}
