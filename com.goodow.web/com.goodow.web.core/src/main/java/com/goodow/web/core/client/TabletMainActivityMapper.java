package com.goodow.web.core.client;

import com.goodow.web.core.shared.MyPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class TabletMainActivityMapper implements ActivityMapper {

  private Place lastPlace;

  @Inject
  private Provider<MyActivity> activity;

  @Inject
  private ReadActivity readActivity;

  private Provider<UIRegistry> registry;

  @Override
  public Activity getActivity(final Place place) {
    MyPlace myPlace = (MyPlace) place;
    if (myPlace.getUri().equals("/read")) {
      return readActivity;
    }
    Activity activity = getActivity(lastPlace, place);
    lastPlace = place;
    return activity;
  }

  private Activity getActivity(final Place lastPlace, final Place newPlace) {
    return activity.get();
  }

}
