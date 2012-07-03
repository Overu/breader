package com.goodow.web.core.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;

import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

public abstract class DetailView extends Composite {

  protected LayoutPanel main;
  protected ScrollPanel scrollPanel;
  protected HeaderPanel headerPanel;
  protected HeaderButton headerBackButton;
  protected HeaderButton headerMainButton;
  protected HTML title;

  public DetailView() {
    main = new LayoutPanel();

    scrollPanel = new ScrollPanel();

    headerPanel = new HeaderPanel();
    title = new HTML();
    headerPanel.setCenterWidget(title);
    headerBackButton = new HeaderButton();
    headerBackButton.setBackButton(true);
    headerBackButton.setVisible(!MGWT.getOsDetection().isAndroid());

    headerMainButton = new HeaderButton();
    headerMainButton.setRoundButton(true);

    if (!MGWT.getOsDetection().isPhone()) {
      headerPanel.setLeftWidget(headerMainButton);
      headerMainButton.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getUtilCss()
          .portraitonly());
    } else {
      headerPanel.setLeftWidget(headerBackButton);
    }

    main.add(headerPanel);
    main.add(scrollPanel);

    initWidget(main);
  }

  public HasTapHandlers getBackbutton() {
    return headerBackButton;
  }

  public HasText getBackbuttonText() {
    return headerBackButton;
  }

  public HasText getHeader() {
    return title;
  }

  public HasTapHandlers getMainButton() {
    return headerMainButton;
  }

  public HasText getMainButtonText() {
    return headerMainButton;
  }

}
