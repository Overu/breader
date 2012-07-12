package com.goodow.web.core.client;

import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.place.shared.Place;

import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.AnimationMapper;

public class WebAnimationMapper implements AnimationMapper {

  @Override
  public Animation getAnimation(final Place oldPlace, final Place newPlace) {
    WebPlace place = (WebPlace) newPlace;
    return place.getAnimation();
  }

}
