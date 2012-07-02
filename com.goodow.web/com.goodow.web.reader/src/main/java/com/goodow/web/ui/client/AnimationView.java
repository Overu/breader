package com.goodow.web.ui.client;

import com.goodow.web.ui.client.places.MyPlace;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.celllist.BasicCell;
import com.googlecode.mgwt.ui.client.widget.celllist.CellListWithHeader;
import com.googlecode.mgwt.ui.client.widget.celllist.HasCellSelectedHandler;

import java.util.List;

public class AnimationView implements IsWidget {

  private CellListWithHeader<MyPlace> list;
  private LayoutPanel main;
  private HeaderPanel headerPanel;
  private HeaderButton headerBackButton;

  /**
	 * 
	 */
  public AnimationView() {
    main = new LayoutPanel();

    headerPanel = new HeaderPanel();

    headerBackButton = new HeaderButton();

    headerPanel.setLeftWidget(headerBackButton);
    headerBackButton.setBackButton(true);
    headerBackButton.setVisible(!MGWT.getOsDetection().isAndroid());

    main.add(headerPanel);

    ScrollPanel scrollPanel = new ScrollPanel();

    list = new CellListWithHeader<MyPlace>(new BasicCell<MyPlace>() {

      @Override
      public boolean canBeSelected(final MyPlace model) {
        return true;
      }

      @Override
      public String getDisplayString(final MyPlace model) {
        return model.getTitle();
      }
    });

    list.getCellList().setRound(true);

    scrollPanel.setWidget(list);
    scrollPanel.setScrollingEnabledX(false);

    main.add(scrollPanel);

  }

  @Override
  public Widget asWidget() {
    return main;
  }

  public HasTapHandlers getBackButton() {
    return headerBackButton;
  }

  public HasCellSelectedHandler getCellSelectedHandler() {
    return list.getCellList();
  }

  public HasText getFirstHeader() {
    return list.getHeader();
  }

  public void setPlaces(final List<MyPlace> places) {
    list.getCellList().render(places);
  }

  public void setLeftButtonText(final String text) {
    headerBackButton.setText(text);

  }

  public void setTitle(final String text) {
    headerPanel.setCenter(text);

  }

}
