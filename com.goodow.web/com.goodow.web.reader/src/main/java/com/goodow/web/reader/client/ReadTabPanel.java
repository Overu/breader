/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.goodow.web.reader.client;

import com.goodow.web.core.shared.MyPlace;
import com.goodow.web.reader.client.style.ReadResources;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabBarButtonBase;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;

import java.util.ArrayList;
import java.util.List;

public class ReadTabPanel extends Composite {

  private LayoutPanel main;

  protected HeaderPanel headerPanel;
  protected HeaderButton rightButton;
  protected HeaderButton leftButton;
  protected HTML title;
  protected TabPanel tabPanel;

  List<String> texts = new ArrayList<String>();

  @Inject
  protected PlaceController placeController;

  public ReadTabPanel() {
    main = new LayoutPanel();

    headerPanel = new HeaderPanel();
    title = new HTML();

    headerPanel.setCenterWidget(title);

    rightButton = new HeaderButton();
    rightButton.setRoundButton(true);
    rightButton.setText("书架");
    rightButton.addTapHandler(new TapHandler() {

      @Override
      public void onTap(final TapEvent event) {
        MyPlace place = new MyPlace(Animation.SWAP, "书架", "/bookshelf");
        placeController.goTo(place);
      }
    });

    leftButton = new HeaderButton();
    leftButton.setRoundButton(true);
    leftButton.setText("设置");

    headerPanel.setLeftWidget(leftButton);
    headerPanel.setRightWidget(rightButton);

    tabPanel = new TabPanel();

    tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

      @Override
      public void onSelection(final SelectionEvent<Integer> event) {
        title.setText(texts.get(event.getSelectedItem()));
      }
    });

    main.add(headerPanel);
    main.add(tabPanel);

    headerPanel.addStyleName(ReadResources.INSTANCE().readHeadCss().headPanel());

    initWidget(main);
  }

  public void add(final TabBarButtonBase button, final Widget child, final String text) {
    texts.add(text);
    tabPanel.add(button, child);
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    title.setText(texts.get(0));
  }
}
