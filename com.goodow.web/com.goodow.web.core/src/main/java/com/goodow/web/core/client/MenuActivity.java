package com.goodow.web.core.client;

import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends MGWTAbstractActivity {

  @Inject
  private PlaceList placelist;

  @Inject
  PlaceController placeController;

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    placelist.setLeftButtonText("Home");
    placelist.setTitle("菜单");
    final List<WebPlace> places = createAnimations();
    placelist.setPlaces(places);
    panel.setWidget(placelist);
  }

  private List<WebPlace> createAnimations() {
    ArrayList<WebPlace> list = new ArrayList<WebPlace>();
    return list;
  }

}
