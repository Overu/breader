package com.goodow.web.core.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class TabletMainActivityMapper implements ActivityMapper {

  @Inject
  private Provider<MyActivity> activity;

  @Inject
  private UIRegistry registry;

  @Override
  public Activity getActivity(final Place place) {
    Activity result = activity.get();
    return result;
  }

}
