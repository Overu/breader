package com.goodow.web.ui.client.activities;

import com.goodow.web.ui.client.event.ActionEvent;
import com.goodow.web.ui.client.event.ActionNames;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class AnimationDoneActivity extends MGWTAbstractActivity {

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    AnimationDoneView view = new AnimationDoneView();

    addHandlerRegistration(view.getBackButton().addTapHandler(new TapHandler() {

      @Override
      public void onTap(final TapEvent event) {
        ActionEvent.fire(eventBus, ActionNames.ANIMATION_END);
      }
    }));

    panel.setWidget(view);
  }

}
