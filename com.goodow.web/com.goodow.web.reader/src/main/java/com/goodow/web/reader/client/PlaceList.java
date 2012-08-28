package com.goodow.web.reader.client;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.core.shared.WebPlace;
import com.goodow.web.reader.shared.ReaderPlace;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedHandler;

public class PlaceList extends FlowView {

  private CellList<WebPlace> cellListWithHeader;

  @Inject
  FlowPanel container;

  @Inject
  EventBus eventBus;

  @Inject
  ReaderPlace reader;

  @Inject
  PlaceController placeController;

  private WebPlace parentPlace;

  private int oldIndex;

  public void addRightWidget(final Widget widget) {
    container.add(widget);
  }

  @Override
  protected void start() {

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
        int index = event.getIndex();
        cellListWithHeader.setSelectedIndex(oldIndex, false);
        cellListWithHeader.setSelectedIndex(index, true);
        oldIndex = index;
        WebPlace child = parentPlace.getChild(index);
        placeController.goTo(child);
      }
    });
    cellListWithHeader.setRound(true);

    // container = new HTMLPanel("");
    container.insert(cellListWithHeader, 0);

    main.add(container);
    // eventBus.addHandler(PlaceChangeEvent.TYPE, this);

    setInput(reader.booksPlace);
  }

  private void setInput(final WebPlace place) {
    this.parentPlace = place;
    cellListWithHeader.render(place.getChildren());
  }

}
