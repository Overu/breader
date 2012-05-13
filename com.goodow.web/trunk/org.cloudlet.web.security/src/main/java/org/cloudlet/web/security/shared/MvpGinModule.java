package org.cloudlet.web.security.shared;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.Place;
import com.google.inject.Provides;

import org.cloudlet.web.mvp.shared.BasePlace;

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
