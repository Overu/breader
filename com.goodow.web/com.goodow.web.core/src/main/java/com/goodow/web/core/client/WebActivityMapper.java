package com.goodow.web.core.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class WebActivityMapper implements ActivityMapper {

  @Inject
  private Provider<WebActivity> activity;

  @Override
  public Activity getActivity(final Place place) {
    Activity result = activity.get();
    return result;
  }

}
