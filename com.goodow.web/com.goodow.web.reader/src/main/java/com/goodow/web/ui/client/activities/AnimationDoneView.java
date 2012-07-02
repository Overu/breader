package com.goodow.web.ui.client.activities;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.RoundPanel;

public class AnimationDoneView implements IsWidget {

  private RoundPanel panel;
  private Button button;

  /**
	 * 
	 */
  public AnimationDoneView() {
    panel = new RoundPanel();
    panel.getElement().setAttribute("style", "text-align:center");
    panel.setHeight("200px");

    HTML html =
        new HTML(
            "<p style='text-align: center; position: relative; top: 75px; font-size: 20px'>great, yeah!<p>");

    panel.add(html);

    button = new Button();
    button.getElement().setAttribute("style",
        "margin:auto;width: 200px; top: 125px; position:relative;");
    button.setText("Back");

    panel.add(button);

  }

  @Override
  public Widget asWidget() {
    return panel;
  }

  public HasTapHandlers getBackButton() {
    return button;
  }

}
