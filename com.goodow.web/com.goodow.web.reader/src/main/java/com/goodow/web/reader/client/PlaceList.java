package com.goodow.web.reader.client;

import com.goodow.web.core.client.WebView;
import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

public class PlaceList extends WebView implements PlaceChangeEvent.Handler {

  private LayoutPanel main;
  private HeaderPanel headerPanel;
  private HeaderButton headerBackButton;
  private CellList<WebPlace> cellListWithHeader;

  @Inject
  EventBus eventBus;

  @Inject
  PlaceController placeController;

  public PlaceList() {
    main = new LayoutPanel();

    main.getElement().setId("testdiv");

    headerPanel = new HeaderPanel();
    main.add(headerPanel);

    headerBackButton = new HeaderButton();
    headerBackButton.setBackButton(true);
    headerBackButton.setText("返回");
    headerPanel.setLeftWidget(headerBackButton);

    ScrollPanel scrollPanel = new ScrollPanel();

    cellListWithHeader = new CellList<WebPlace>(new BasicCell<WebPlace>() {
      @Override
      public boolean canBeSelected(final WebPlace place) {
        return true;
      }

      @Override
      public String getDisplayString(final WebPlace place) {
        return place.getTitle();
      }
    });
    cellListWithHeader.setRound(true);
    scrollPanel.setWidget(cellListWithHeader);
    scrollPanel.setScrollingEnabledX(false);

    main.add(scrollPanel);
    initWidget(main);
  }

  @Override
  public void onPlaceChange(final PlaceChangeEvent event) {
    WebPlace place = (WebPlace) event.getNewPlace();
    setInput(place);
  }

  @Override
  protected void start() {
    eventBus.addHandler(PlaceChangeEvent.TYPE, this);
    WebPlace place = (WebPlace) placeController.getWhere();
    setInput(place);
  }

  private void setInput(final WebPlace place) {
    cellListWithHeader.render(place.getChildren());
    if (place.getParent() == null) {
      headerBackButton.setVisible(false);
    } else {
      headerBackButton.setVisible(true);
    }
  }

}
