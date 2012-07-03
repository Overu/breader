package com.goodow.web.core.client;


import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;

public class TabletNavActivityMapper implements ActivityMapper {

  @Inject
  MenuActivity menu;

  @Override
  public Activity getActivity(final Place place) {
    return menu;
  }
}
