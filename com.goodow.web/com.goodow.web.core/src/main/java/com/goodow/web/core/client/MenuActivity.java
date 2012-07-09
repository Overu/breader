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

  @Inject
  PlaceController placeController;

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
            MyPlace place = places.get(index);
            placeController.goTo(place);
          }
        }));

    panel.setWidget(view);

  }

  /**
   * @return
   */
  private List<MyPlace> createAnimations() {
    ArrayList<MyPlace> list = new ArrayList<MyPlace>();
    list.add(new MyPlace(Animation.SWAP, "书架", "/bookshelf"));
    list.add(new MyPlace(Animation.FADE, "书城", "/bookstore"));
    return list;
  }

}
