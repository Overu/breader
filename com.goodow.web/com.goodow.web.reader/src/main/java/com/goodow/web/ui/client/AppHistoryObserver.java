package com.goodow.web.ui.client;

import com.goodow.web.ui.client.event.ActionEvent;
import com.goodow.web.ui.client.event.ActionNames;
import com.goodow.web.ui.client.places.MyPlace;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.History;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

import com.googlecode.mgwt.dom.client.event.mouse.HandlerRegistrationCollection;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.history.HistoryHandler;
import com.googlecode.mgwt.mvp.client.history.HistoryObserver;

public class AppHistoryObserver implements HistoryObserver {

  @Override
  //
  public HandlerRegistration bind(final EventBus eventBus, final HistoryHandler historyHandler) {
    HandlerRegistration addHandler =
        eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
          @Override
          public void onPlaceChange(final PlaceChangeEvent event) {
            Place place = event.getNewPlace();
            // if (MGWT.getOsDetection().isTablet()) {
            // historyHandler.replaceCurrentPlace(place);
            // historyHandler.goTo(place, true);
            // } else {
            // historyHandler.goTo(place);
            // }
            // historyHandler.goTo(place);
          }
        });

    HandlerRegistration register2 =
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler() {

          @Override
          public void onAction(final ActionEvent event) {

            History.back();

          }
        });

    HandlerRegistration register =
        ActionEvent.register(eventBus, ActionNames.ANIMATION_END, new ActionEvent.Handler() {

          @Override
          public void onAction(final ActionEvent event) {
            History.back();
          }
        });

    HandlerRegistrationCollection col = new HandlerRegistrationCollection();
    col.addHandlerRegistration(register);
    col.addHandlerRegistration(register2);
    col.addHandlerRegistration(addHandler);
    return col;
  }

  @Override
  public void onAppStarted(final Place place, final HistoryHandler historyHandler) {
    historyHandler.replaceCurrentPlace(new MyPlace(Animation.FADE, "Home", "home"));
  }

  @Override
  public void onHistoryChanged(final Place place, final HistoryHandler handler) {

  }

  @Override
  public void onPlaceChange(final Place place, final HistoryHandler handler) {

  }

}
