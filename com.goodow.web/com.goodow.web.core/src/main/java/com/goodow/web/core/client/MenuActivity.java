package com.goodow.web.core.client;

import com.goodow.web.core.shared.MyPlace;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import com.googlecode.mgwt.mvp.client.Animation;
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
    final List<MyPlace> places = createAnimations();
    placelist.setPlaces(places);
    panel.setWidget(placelist);
  }

  private List<MyPlace> createAnimations() {
    ArrayList<MyPlace> list = new ArrayList<MyPlace>();
    list.add(new MyPlace(Animation.FLIP, "书架", "/bookshelf"));
    list.add(new MyPlace(Animation.FADE, "书城", "/bookstore"));
    return list;
  }

}
