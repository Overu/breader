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
package com.goodow.wave.client.shell;

import com.goodow.web.feature.client.FeatureDetection;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class WidgetContainer extends FlowPanel implements AcceptsOneWidget {
  private Widget previousWidget;
  private Widget currentWidget;
  private long toRotateY = 0;
  private boolean toContainerFront = true;
  private static final com.goodow.wave.client.shell.WaveShellResources.Style CSS =
      WaveShellResources.css();;
  private static final String[] STYLE_NAMES = new String[] {
      CSS.widgetPreviousBegin(), CSS.widgetPreviousEnd(), CSS.widgetCurrentBegin(),
      CSS.widgetCurrentEnd()};

  private static native void setTransitionEndListener(Element elem, EventListener listener)/*-{
                                                                                           var callBack = function(e){
                                                                                           listener.@com.google.gwt.user.client.EventListener::onBrowserEvent(Lcom/google/gwt/user/client/Event;)(e);
                                                                                           };
                                                                                           elem.addEventListener('webkitTransitionEnd', callBack, false);
                                                                                           }-*/;

  @Inject
  WidgetContainer() {
    addStyleName(WaveShellResources.css().widgetContainer());
  }

  @Override
  public void setWidget(final IsWidget w) {
    Widget newWidget = asWidgetOrNull(w);
    if (newWidget == null) {
      return;
    }
    // Remove old child.
    if (previousWidget != null) {
      remove(previousWidget);
    }
    if (currentWidget != null) {
      toRotateY += 180;
      toContainerFront = !toContainerFront;
    }
    previousWidget = currentWidget;
    currentWidget = newWidget;

    // if (previousWidget != null) {
    // previousWidget.removeStyleName(css.widgetCurrent());
    // previousWidget.addStyleName(css.widgetPrevious());
    // }
    // currentWidget.removeStyleName(css.widgetPrevious());
    // currentWidget.addStyleName(css.widgetCurrent());
    Style curStyle = currentWidget.getElement().getStyle();
    // curStyle.setProperty("webkitTransform", !toContainerFront ? "rotateY(180deg)" : "initial");
    curStyle.setTop(0, Unit.PX);
    setStyle(currentWidget, CSS.widgetCurrentBegin());
    setStyle(previousWidget, CSS.widgetPreviousBegin());
    insert(currentWidget, 0);
    FeatureDetection.hideAddressBar();
    if (previousWidget != null) {
      setTransitionEndListener(previousWidget.getElement(), new EventListener() {

        @Override
        public void onBrowserEvent(final Event event) {
          previousWidget.removeFromParent();
        }
      });
    }
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

      @Override
      public void execute() {
        setStyle(currentWidget, CSS.widgetCurrentEnd());
        setStyle(previousWidget, CSS.widgetPreviousEnd());
        if (previousWidget != null) {
          previousWidget.getElement().getStyle().setTop(-1 * currentWidget.getOffsetHeight() - 7,
              Unit.PX);
        }
      }
    });
  }

  private void setStyle(final Widget widget, final String name) {
    if (widget == null) {
      return;
    }
    for (String n : STYLE_NAMES) {
      widget.removeStyleName(n);
    }
    widget.addStyleName(name);
  }
}
