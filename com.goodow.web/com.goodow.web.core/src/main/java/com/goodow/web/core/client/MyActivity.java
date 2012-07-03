package com.goodow.web.core.client;

import com.goodow.web.core.client.event.ActionEvent;
import com.goodow.web.core.client.event.ActionNames;
import com.goodow.web.core.shared.MyPlace;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class MyActivity extends MGWTAbstractActivity {

  @Inject
  PlaceController placeController;

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {

    MyView view = new MyView();

    addHandlerRegistration(view.getBackButton().addTapHandler(new TapHandler() {

      @Override
      public void onTap(final TapEvent event) {
        ActionEvent.fire(eventBus, ActionNames.ANIMATION_END);
      }
    }));
    MyPlace place = (MyPlace) placeController.getWhere();
    view.setTitle(place.getTitle());
    panel.setWidget(view);
  }

}
