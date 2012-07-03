package com.goodow.web.core.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.HTML;

import com.googlecode.mgwt.ui.client.widget.Carousel;
import com.googlecode.mgwt.ui.client.widget.RoundPanel;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

public class ReadView extends DetailView {

  private Carousel carousel;

  public ReadView() {

    scrollPanel.removeFromParent();

    carousel = new Carousel();

    main.add(carousel);

    for (int i = 0; i < 5; i++) {

      ScrollPanel scrollPanel2 = new ScrollPanel();
      scrollPanel2.setScrollingEnabledX(false);
      // scrollPanel2.setWidth("100%");

      RoundPanel flowPanel3 = new RoundPanel();
      for (int j = 0; j < 10; j++) {
        HTML html = new HTML("Slide: " + (i + 1));
        html.getElement().getStyle().setMarginBottom(300, Unit.PX);
        flowPanel3.add(html);
      }

      scrollPanel2.setWidget(flowPanel3);

      carousel.add(scrollPanel2);
    }

  }

  public HasSelectionHandlers<Integer> getSwipePanel() {
    return carousel;
  }

}
