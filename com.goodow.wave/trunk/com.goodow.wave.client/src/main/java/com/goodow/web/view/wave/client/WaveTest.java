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
package com.goodow.web.view.wave.client;

import com.goodow.wave.client.wavepanel.WavePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.logging.Logger;

@Singleton
public class WaveTest extends WavePanel {

  interface Bundle extends ClientBundle {
    @Source("WaveTest.css")
    Style style();
  }

  interface Style extends CssResource {

    String test();
  }

  private static final Logger logger = Logger.getLogger(WaveTest.class.getName());
  private static final Bundle INSTANCE;
  // private boolean start = false;
  // private int x1;
  // private int x2;
  JsArray<Touch> touches = null;
  // private boolean scheduled;
  // private boolean isStart = false;
  private boolean scheduledTwo = false;
  private boolean scheduledOne = false;

  private int startX1;
  private int startX2;

  static {
    logger.finest("static init start");

    INSTANCE = GWT.create(Bundle.class);
    INSTANCE.style().ensureInjected();

    logger.finest("static init end");
  }

  @Inject
  public WaveTest() {
    // getWaveTitle().setText("test title");
    // wave.setHeader(new Label("test header"));
    // setWaveContent(new Label("test content"));
    // ToolBarClickButton btn = addWaveToolBar().addClickButton();
    // btn.setText("test");
    // wave.setFooter(new Label("test footer"));
    // ToolBarClickButton clickButton = wave.toolbar().addClickButton();
    // clickButton.setText("test button");
    HTMLPanel hp =
        new HTMLPanel(
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Nulla at turpis eget nibh ultricies dignissim. Duis luctus euismod turpis. Mauris augue. Aliquam facilisis semper elit. Pellentesque semper hendrerit arcu. Phasellus eleifend commodo justo. Aliquam orci urna, imperdiet sit amet, posuere in, lobortis et, risus. Integer interdum nonummy erat. Nullam tellus. Sed accumsan. Vestibulum orci ipsum, eleifend vitae, mollis vel, mollis sed, purus. Suspendisse mollis elit eu magna. Morbi egestas. Nunc leo ipsum, blandit ac, viverra quis, porttitor quis, dui. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vivamus scelerisque ipsum ut justo. Pellentesque et ligula eu massa sagittis rutrum. In urna nibh, eleifend vel, suscipit ut, sagittis id, nunc."
                + "Nam ut sapien sed pede pulvinar rutrum. Nunc eu elit sed augue aliquet tincidunt. Morbi rutrum. Vestibulum dui turpis, lobortis quis, euismod sed, consectetuer sit amet, nunc. Nam mi. Fusce at nisl eu tortor bibendum eleifend. Sed ac metus. Phasellus nec elit. Morbi tortor nulla, tristique a, adipiscing at, consectetuer et, nisi. Nunc vel sapien sed risus hendrerit egestas. Vivamus turpis arcu, placerat eu, congue vel, commodo ut, nisl.&lt;");
    hp.getElement().getStyle().setProperty("webkitColumnWidth", 100 + "px");
    hp.setWidth("100%");
    hp.setHeight("700px");
    HTMLPanel test = new HTMLPanel("");
    test.addStyleName(INSTANCE.style().test());
    hp.add(test);
    setWaveContent(hp);

    // hp.addDomHandler(new TouchStartHandler() {
    //
    // @Override
    // public void onTouchStart(final TouchStartEvent event) {
    // event.preventDefault();
    // JsArray<Touch> touches = event.getTouches();
    // if (touches.length() == 2) {
    // Touch touch1 = touches.get(0);
    // Touch touch2 = touches.get(1);
    // x1 = touch1.getPageX();
    // x2 = touch2.getPageX();
    // start = true;
    // Window.alert("sdf");
    // }
    // }
    // }, TouchStartEvent.getType());
    //
    // hp.addDomHandler(new TouchMoveHandler() {
    //
    // @Override
    // public void onTouchMove(final TouchMoveEvent event) {
    // event.preventDefault();
    // if (start) {
    // JsArray<Touch> touches = event.getTouches();
    // Touch touch1 = touches.get(0);
    // Touch touch2 = touches.get(1);
    // int touch1PageX = touch1.getPageX();
    // int touch2PageX = touch2.getPageX();
    // int left = touch1PageX - x1;
    // int right = touch2PageX - x2;
    // if (left > 0 && right > 0 && (left > 100 || right > 100)) {
    // History.forward();
    // start = false;
    // return;
    // } else if (left < 0 && right < 0 && (left < -100 || right < -100)) {
    // History.back();
    // start = false;
    // return;
    // }
    // }
    // }
    // }, TouchMoveEvent.getType());
    //
    // hp.addDomHandler(new TouchEndHandler() {
    //
    // @Override
    // public void onTouchEnd(final TouchEndEvent event) {
    // event.preventDefault();
    // start = false;
    // }
    // }, TouchEndEvent.getType());

    // hp.addDomHandler(new TouchStartHandler() {
    //
    // @Override
    // public void onTouchStart(final TouchStartEvent event) {
    // JsArray<Touch> toucheStart = event.getTouches();
    // if (toucheStart.length() >= 2) {
    // logger.info("touch start:" + toucheStart.length());
    // isStart = true;
    // Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
    //
    // @Override
    // public boolean execute() {
    // if (touches != null && isStart && !scheduled) {
    // scheduled = true;
    // Scheduler.get().scheduleDeferred(new ScheduledCommand() {
    //
    // @Override
    // public void execute() {
    // scheduled = false;
    // printLog(touches);
    // }
    // });
    // }
    // if (!isStart) {
    // logger.info("Scheduler end:" + isStart);
    // }
    // return isStart;
    // }
    // }, 15);
    // }
    // }
    // }, TouchStartEvent.getType());
    //
    // hp.addDomHandler(new TouchMoveHandler() {
    //
    // @Override
    // public void onTouchMove(final TouchMoveEvent event) {
    // touches = event.getTouches();
    // }
    // }, TouchMoveEvent.getType());
    //
    // hp.addDomHandler(new TouchEndHandler() {
    //
    // @Override
    // public void onTouchEnd(final TouchEndEvent event) {
    // touches = null;
    // logger.info("strat end:");
    // isStart = false;
    // }
    // }, TouchEndEvent.getType());

    hp.addDomHandler(new TouchStartHandler() {

      @Override
      public void onTouchStart(final TouchStartEvent event) {
        JsArray<Touch> touchesStart = event.getTouches();
        switch (touchesStart.length()) {
          case 1:
            logger.info("touches: 1 ");
            scheduledTwo = false;
            break;

          case 2:
            logger.info("touches: 2 ");
            scheduledTwo = false;
            break;

          default:
            break;
        }
      }
    }, TouchStartEvent.getType());

    hp.addDomHandler(new TouchMoveHandler() {

      @Override
      public void onTouchMove(final TouchMoveEvent event) {
        event.preventDefault();
        JsArray<Touch> touchesMove = event.getTouches();
        switch (touchesMove.length()) {
          case 1:
            if (scheduledOne) {
              return;
            }
            scheduledOne = true;
            printLog(touchesMove);
            break;

          case 2:
            if (scheduledTwo) {
              return;
            }
            scheduledTwo = true;
            Touch touch1 = touchesMove.get(0);
            Touch touch2 = touchesMove.get(1);
            int nowX1 = touch1.getPageX();
            int nowX2 = touch2.getPageX();
            int subtractX1 = nowX1 - startX1;
            int subtractX2 = nowX2 - startX2;
            if (subtractX1 > 0 && subtractX2 > 0) {

              return;
            } else if (subtractX1 < 0 && subtractX2 < 0) {

              return;
            }
            printLog(touchesMove);
            break;

          default:
            break;
        }
      }

    }, TouchMoveEvent.getType());

    hp.addDomHandler(new TouchEndHandler() {

      @Override
      public void onTouchEnd(final TouchEndEvent event) {
        scheduledOne = false;
        scheduledTwo = false;
      }
    }, TouchEndEvent.getType());

  }

  private void printLog(final JsArray<Touch> touches) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < touches.length(); i++) {
      Touch touch = touches.get(i);
      sb.append("{touch-id:" + touch.getIdentifier());
      sb.append(";touch-pageX:" + touch.getPageX());
      sb.append(";touch-pageY:" + touch.getPageY());
      sb.append(";touch-clientX:" + touch.getClientX());
      sb.append(";touch-clientY:" + touch.getClientY());
      sb.append(";touch-screenX:" + touch.getScreenX());
      sb.append(";touch-screenY:" + touch.getScreenY());
      sb.append("}");
    }
    logger.info(sb.toString());
  }
}
