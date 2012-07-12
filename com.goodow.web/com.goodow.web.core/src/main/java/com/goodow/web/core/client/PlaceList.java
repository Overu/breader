package com.goodow.web.core.client;

import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;

import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.celllist.BasicCell;
import com.googlecode.mgwt.ui.client.widget.celllist.CellListWithHeader;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedHandler;

import java.util.List;

public class PlaceList extends Composite {

  private CellListWithHeader<WebPlace> list;
  private LayoutPanel main;
  private HeaderPanel headerPanel;
  private HeaderButton headerBackButton;
  private List<WebPlace> places;

  @Inject
  public PlaceList(final PlaceController placeController) {
    main = new LayoutPanel();

    headerPanel = new HeaderPanel();

    headerBackButton = new HeaderButton();

    headerPanel.setLeftWidget(headerBackButton);
    headerBackButton.setBackButton(true);
    headerBackButton.setVisible(!MGWT.getOsDetection().isAndroid());

    main.add(headerPanel);

    ScrollPanel scrollPanel = new ScrollPanel();

    list = new CellListWithHeader<WebPlace>(new BasicCell<WebPlace>() {

      @Override
      public boolean canBeSelected(final WebPlace model) {
        return true;
      }

      @Override
      public String getDisplayString(final WebPlace model) {
        return model.getTitle();
      }
    });

    list.getCellList().setRound(true);

    scrollPanel.setWidget(list);
    scrollPanel.setScrollingEnabledX(false);

    list.getCellList().addCellSelectedHandler(new CellSelectedHandler() {

      @Override
      public void onCellSelected(final CellSelectedEvent event) {
        int index = event.getIndex();
        WebPlace place = places.get(index);
        placeController.goTo(place);
      }
    });
    main.add(scrollPanel);
    initWidget(main);
  }

  public HasTapHandlers getBackButton() {
    return headerBackButton;
  }

  public HasText getFirstHeader() {
    return list.getHeader();
  }

  public void setLeftButtonText(final String text) {
    headerBackButton.setText(text);

  }

  public void setPlaces(final List<WebPlace> places) {
    this.places = places;
    list.getCellList().render(places);
  }

  @Override
  public void setTitle(final String text) {
    headerPanel.setCenter(text);

  }

}
