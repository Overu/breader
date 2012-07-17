package com.goodow.web.reader.client;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedHandler;

public class PlaceList extends FlowView implements PlaceChangeEvent.Handler {

  private HeaderPanel headerPanel;
  private HeaderButton headerBackButton;
  private CellList<WebPlace> cellListWithHeader;

  @Inject
  FlowPanel container;

  @Inject
  EventBus eventBus;

  @Inject
  ReaderPlugin reader;

  @Inject
  PlaceController placeController;

  public void addRightWidget(final Widget widget) {
    container.add(widget);
  }

  @Override
  public void onPlaceChange(final PlaceChangeEvent event) {
    // WebPlace place = (WebPlace) event.getNewPlace();
    setInput(reader.booksPlace);
  }

  @Override
  protected void start() {

    headerPanel = new HeaderPanel();
    main.add(headerPanel);

    headerBackButton = new HeaderButton();
    headerBackButton.setBackButton(true);
    headerBackButton.setText("返回");
    headerPanel.setLeftWidget(headerBackButton);
    headerBackButton.addTapHandler(new TapHandler() {

      @Override
      public void onTap(final TapEvent event) {
        placeController.goTo(reader.bookstorePlace);
      }
    });

    cellListWithHeader = new CellList<WebPlace>(new BookCell<WebPlace>() {
      @Override
      public boolean canBeSelected(final WebPlace place) {
        return true;
      }

      @Override
      public String getDisplayString(final WebPlace place) {
        return place.getTitle();
      }
    });
    cellListWithHeader.addCellSelectedHandler(new CellSelectedHandler() {
      @Override
      public void onCellSelected(final CellSelectedEvent event) {
        WebPlace place = reader.booksPlace.getChild(event.getIndex());
        placeController.goTo(place);
      }
    });
    cellListWithHeader.setRound(true);

    // container = new HTMLPanel("");
    container.insert(cellListWithHeader, 0);

    main.add(container);
    eventBus.addHandler(PlaceChangeEvent.TYPE, this);

    setInput(reader.booksPlace);
  }

  private void setInput(final WebPlace place) {

    cellListWithHeader.render(place.getChildren());
    if (place.getParent() == reader.booksPlace) {
      headerBackButton.setVisible(false);
    } else {
      headerBackButton.setVisible(true);
    }
  }

}
