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
package com.goodow.web.ui.client.shell.one;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ActivityPanel extends SimplePanel {

  private String side;
  private final Provider<OnePageShell> shell;

  @Inject
  ActivityPanel(final Provider<OnePageShell> shell) {
    this.shell = shell;
  }

  public String getSide() {
    return side;
  }

  public void setSide(final String side) {
    this.side = side;
    getElement().getStyle().setFloat(Style.Float.valueOf(side.toUpperCase()));
  }

  @Override
  public void setWidget(final IsWidget w) {
    if (w == null) {
      setVisible(false);
    } else {
      if (!shell.get().isAttached()) {
        // RootLayoutPanel.get().clear();
        RootPanel.get().clear();
        RootPanel.get().add(shell.get());
      }
      setVisible(true);
    }
    super.setWidget(w);

    if (side != null && getParent() instanceof ComplexPanel) {
      ComplexPanel parent = (ComplexPanel) getParent();
      int index = parent.getWidgetIndex(this);

      if (parent.getWidgetCount() > index + 1) {
        Widget centerWidget = parent.getWidget(index + 1);

        int margin = w == null ? 10 : getOffsetWidth();

        if ("left".equals(side)) {
          centerWidget.getElement().getStyle().setMarginLeft(margin, Unit.PX);
        } else if ("right".equals(side)) {
          centerWidget.getElement().getStyle().setMarginRight(margin, Unit.PX);
        }
      }
    }
  }
}
