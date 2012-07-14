package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.user.client.ui.Composite;

import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

public class BookList extends Composite {

  private LayoutPanel main;
  private HeaderPanel headerPanel;
  private HeaderButton headerBackButton;
  private CellList<Book> cellListWithHeader;

  public BookList() {
    main = new LayoutPanel();

    main.getElement().setId("testdiv");

    headerPanel = new HeaderPanel();
    main.add(headerPanel);

    headerBackButton = new HeaderButton();
    headerBackButton.setBackButton(true);
    headerPanel.setLeftWidget(headerBackButton);
    headerBackButton.setVisible(!MGWT.getOsDetection().isAndroid());

    ScrollPanel scrollPanel = new ScrollPanel();

    cellListWithHeader = new CellList<Book>(new BasicCell<Book>() {

      @Override
      public boolean canBeSelected(final Book model) {
        return true;
      }

      @Override
      public String getDisplayString(final Book model) {
        return model.getTitle();
      }
    });
    cellListWithHeader.setRound(true);
    scrollPanel.setWidget(cellListWithHeader);
    scrollPanel.setScrollingEnabledX(false);

    main.add(scrollPanel);
    initWidget(main);
  }

}
