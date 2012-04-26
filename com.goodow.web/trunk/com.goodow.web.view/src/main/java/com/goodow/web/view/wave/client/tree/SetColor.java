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
package com.goodow.web.view.wave.client.tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Singleton;

@Singleton
public class SetColor extends PopupPanel {

  interface Binder extends UiBinder<Widget, SetColor> {
  }

  // private class EventListenerImpl implements EventListener {
  //
  // @Override
  // public void onBrowserEvent(final Event event) {
  // if (DOM.eventGetType(event) == Event.ONCLICK) {
  // Element elm = Element.as(event.getEventTarget());
  // Window.alert("title:" + elm.getTitle() + ";color:" + elm.getStyle().getBackgroundColor());
  // }
  // }
  //
  // }

  private static Binder binder = GWT.create(Binder.class);
  private static Map<String, String> colorMap = new LinkedHashMap<String, String>();

  private Element changeElm;
  // private EventListenerImpl eventListener = new EventListenerImpl();

  static {
    colorMap.put("Red", "hsl(0, 77%, 55%)");
    colorMap.put("Dark Orange", "hsl(27, 100%, 45%)");
    colorMap.put("Orange", "hsl(36, 100%, 50%)");
    colorMap.put("Brown", "hsl(34, 55%, 25%)");
    colorMap.put("Beige", "hsl(34, 48%, 76%)");
    colorMap.put("Yellow", "hsl(54, 100%, 50%)");

    colorMap.put("Green", "hsl(120, 100%, 30%)");
    colorMap.put("Dark Green", "hsl(120, 100%, 20%)");
    colorMap.put("Light Green", "hsl(71, 100%, 37%)");
    colorMap.put("Yellow-Green", "hsl(67, 100%, 41%)");
    colorMap.put("Aqua", "hsl(132, 61%, 61%)");
    colorMap.put("Cyan", "hsl(190, 100%, 50%)");

    colorMap.put("Blue", "hsl(214, 77%, 55%)");
    colorMap.put("Dark Blue", "hsl(220, 100%, 30%)");
    colorMap.put("Violet", "hsl(248, 77%, 55%)");
    colorMap.put("Purple", "hsl(274, 77%, 55%)");
    colorMap.put("Dark Pink", "hsl(292, 77%, 55%)");
    colorMap.put("Pink", "hsl(300, 85%, 57%)");

    colorMap.put("White", "white");
    colorMap.put("20% Black", "hsl(0, 0%, 80%)");
    colorMap.put("40% Black", "hsl(0, 0%, 60%)");
    colorMap.put("60% Black", "hsl(0, 0%, 40%)");
    colorMap.put("80% Black", "hsl(0, 0%, 20%)");
    colorMap.put("Black", "black");
  }

  @UiField
  DivElement colors;
  @UiField
  Element noColor;

  public SetColor() {
    Widget widget = binder.createAndBindUi(this);
    this.getElement().getStyle().setWidth(129, Unit.PX);
    this.add(widget);
    this.setAutoHideEnabled(true);
    NodeList<Node> childNodes = colors.getChildNodes();
    putColor(childNodes);

    DOM.sinkEvents((com.google.gwt.user.client.Element) noColor, Event.ONCLICK);
    DOM.setEventListener((com.google.gwt.user.client.Element) noColor, new EventListener() {

      @Override
      public void onBrowserEvent(final Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK) {
          NodeList<Element> aTags = changeElm.getElementsByTagName("span");
          Element aTag = aTags.getItem(1);
          Style aTagStyle = aTag.getStyle();
          aTagStyle.clearColor();
          aTagStyle.clearBackgroundColor();
          aTagStyle.clearBorderColor();
          aTagStyle.clearBorderStyle();
          aTagStyle.clearBorderWidth();
          aTagStyle.clearTextDecoration();
        }
      }
    });
  }

  @Override
  public void onBrowserEvent(final Event event) {
    if (DOM.eventGetType(event) == Event.ONCLICK) {
      if (changeElm == null) {
        Window.alert("changeElm is null!");
        return;
      }

      NodeList<Element> aTags = changeElm.getElementsByTagName("span");
      Element aTag = aTags.getItem(1);
      Element elm = Element.as(event.getEventTarget());
      Style elmStyle = elm.getStyle();
      Style aTagStyle = aTag.getStyle();

      if (elm.getTitle().equals("White") || elm.getTitle().equals("20% Black")) {
        aTagStyle.setBackgroundColor(elmStyle.getBackgroundColor());
        aTagStyle.setColor("black");
        aTagStyle.setBorderStyle(BorderStyle.SOLID);
        aTagStyle.setBorderWidth(1, Unit.PX);
        aTagStyle.setBorderColor("black");
        aTagStyle.setTextDecoration(TextDecoration.NONE);
      } else {
        if (!aTagStyle.getBorderWidth().equals("")) {
          aTagStyle.clearBorderColor();
          aTagStyle.clearBorderStyle();
          aTagStyle.clearBorderWidth();
        }
        aTagStyle.setTextDecoration(TextDecoration.NONE);
        aTagStyle.setBackgroundColor(elmStyle.getBackgroundColor());
        aTagStyle.setColor("white");
      }
      // Window.alert("title:" + elm.getTitle() + ";color:" + elm.getStyle().getBackgroundColor());
    }
  }

  public void setChangeElm(final Element changeElm) {
    this.changeElm = changeElm;
  }

  private void addEvent(final Element elm) {
    DOM.sinkEvents((com.google.gwt.user.client.Element) elm, Event.ONCLICK);
    DOM.setEventListener((com.google.gwt.user.client.Element) elm, this);
    // new EventListener() {
    //
    // @Override
    // public void onBrowserEvent(final Event event) {
    // if (DOM.eventGetType(event) == Event.ONCLICK) {
    // Window.alert("title:" + elm.getTitle() + ";color:" + elm.getStyle().getBackgroundColor());
    // }
    // }
    // });
  }

  private void putColor(final NodeList<Node> childNodes) {
    Iterator<Entry<String, String>> iterator = colorMap.entrySet().iterator();
    int i = 1;
    while (iterator.hasNext()) {
      Entry<String, String> color = iterator.next();
      Element elm = Element.as(childNodes.getItem(i));
      elm.setTitle(color.getKey());
      elm.getStyle().setBackgroundColor(color.getValue());
      addEvent(elm);
      i = i + 2;
    }
  }
}
