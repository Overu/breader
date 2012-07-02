package com.goodow.web.ui.client;

import com.goodow.web.ui.client.activities.AnimationDoneActivity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class TabletMainActivityMapper implements ActivityMapper {

  private Place lastPlace;

  @Inject
  private Provider<AnimationDoneActivity> activity;

  @Override
  public Activity getActivity(final Place place) {
    Activity activity = getActivity(lastPlace, place);
    lastPlace = place;
    return activity;
  }

  private Activity getActivity(final Place lastPlace, final Place newPlace) {
    return activity.get();
  }

}
