package com.goodow.web.core.shared;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MyPlaceMapper implements PlaceHistoryMapper {

  @HomePlace
  @Inject
  WebPlace homePlace;

  @Override
  public Place getPlace(final String token) {
    WebPlace place = homePlace.findChild(token);
    return place;
  }

  @Override
  public String getToken(final Place place) {
    WebPlace p = (WebPlace) place;
    return p.getUri();
  }

}
