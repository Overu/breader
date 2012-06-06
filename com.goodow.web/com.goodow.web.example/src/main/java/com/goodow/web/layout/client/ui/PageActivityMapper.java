package com.goodow.web.layout.client.ui;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public final class PageActivityMapper implements ActivityMapper {

  @Override
  public Activity getActivity(Place place) {
    return (Presenter) place;
  }

}
