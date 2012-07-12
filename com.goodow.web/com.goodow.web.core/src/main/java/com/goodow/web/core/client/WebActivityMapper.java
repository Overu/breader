package com.goodow.web.core.client;

import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class WebActivityMapper implements ActivityMapper {

  @Inject
  private Provider<WebActivity> activity;

  private Activity lastActivity;

  private WebPlace lastPlace;

  @Override
  public Activity getActivity(final Place place) {
    WebPlace newPlace = (WebPlace) place;

    lastActivity = activity.get();
    lastPlace = newPlace;
    return lastActivity;
  }

}
