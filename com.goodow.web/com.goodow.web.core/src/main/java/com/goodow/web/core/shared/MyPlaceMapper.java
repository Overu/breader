package com.goodow.web.core.shared;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Singleton;

@Singleton
public class MyPlaceMapper implements PlaceHistoryMapper {

  @HomePlace
  MyPlace homePlace;

  @Override
  public Place getPlace(final String token) {
    MyPlace place = homePlace.getChild(token);
    // TODO null?
    return place;
  }

  @Override
  public String getToken(final Place place) {
    MyPlace p = (MyPlace) place;
    return p.getUri();
  }

}
