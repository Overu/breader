package com.goodow.web.ui.client;

import com.goodow.web.ui.client.places.MyPlace;

import com.google.gwt.place.shared.Place;

import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.AnimationMapper;

public class TabletMainAnimationMapper implements AnimationMapper {

  @Override
  public Animation getAnimation(final Place oldPlace, final Place newPlace) {
    if (oldPlace == null) {
      return Animation.FADE;
    }
    MyPlace place = (MyPlace) newPlace;
    return place.getAnimation();
  }

}
