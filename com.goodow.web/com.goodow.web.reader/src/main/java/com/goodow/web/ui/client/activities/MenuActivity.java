package com.goodow.web.ui.client.activities;

import com.goodow.web.ui.client.event.ActionEvent;
import com.goodow.web.ui.client.event.ActionNames;
import com.goodow.web.ui.client.places.MyPlace;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedHandler;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends MGWTAbstractActivity {

  @Inject
  private AnimationView view;

  private List<MyPlace> places;

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {

    view.setLeftButtonText("Home");
    view.setTitle("Animation");
    places = createAnimations();
    view.setPlaces(places);

    addHandlerRegistration(view.getBackButton().addTapHandler(new TapHandler() {

      @Override
      public void onTap(final TapEvent event) {
        ActionEvent.fire(eventBus, ActionNames.BACK);
      }
    }));

    addHandlerRegistration(view.getCellSelectedHandler().addCellSelectedHandler(
        new CellSelectedHandler() {

          @Override
          public void onCellSelected(final CellSelectedEvent event) {
            int index = event.getIndex();

            AnimationSelectedEvent.fire(eventBus, places.get(index));

          }
        }));

    panel.setWidget(view);

  }

  /**
   * @return
   */
  private List<MyPlace> createAnimations() {
    ArrayList<MyPlace> list = new ArrayList<MyPlace>();

    list.add(new MyPlace(Animation.SLIDE, "Slide", "slide"));
    list.add(new MyPlace(Animation.SLIDE_UP, "Slide up", "Slide up"));
    list.add(new MyPlace(Animation.DISSOLVE, "Dissolve", "Dissolve"));
    list.add(new MyPlace(Animation.FADE, "Fade", "Fade"));
    list.add(new MyPlace(Animation.FLIP, "Flip", "Flip"));
    list.add(new MyPlace(Animation.POP, "Pop", "Pop"));
    list.add(new MyPlace(Animation.SWAP, "Swap", "Swap"));
    // list.add(new Animation("Cube"));

    return list;
  }

}
