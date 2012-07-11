package com.goodow.web.core.shared;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MyPlaceMapper implements PlaceHistoryMapper {

  @HomePlace
  @Inject
  MyPlace homePlace;

  @Override
  public Place getPlace(final String token) {
    MyPlace place = homePlace.findChild(token);
    return place;
  }

  @Override
  public String getToken(final Place place) {
    MyPlace p = (MyPlace) place;
    return p.getUri();
  }

}
