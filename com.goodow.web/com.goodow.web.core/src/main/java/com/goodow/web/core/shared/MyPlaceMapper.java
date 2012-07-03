package com.goodow.web.core.shared;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Singleton;

import com.googlecode.mgwt.mvp.client.Animation;

@Singleton
public class MyPlaceMapper implements PlaceHistoryMapper {

  @Override
  public Place getPlace(final String token) {
    MyPlace place = new MyPlace(Animation.SLIDE, token, token);
    place.setUri(token);
    return place;
  }

  @Override
  public String getToken(final Place place) {
    MyPlace p = (MyPlace) place;
    return p.getUri();
  }

}
