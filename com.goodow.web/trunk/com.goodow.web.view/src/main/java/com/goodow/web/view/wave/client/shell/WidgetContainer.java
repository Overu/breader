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
package com.goodow.web.view.wave.client.shell;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class WidgetContainer extends FlowPanel implements AcceptsOneWidget {
  private Widget previousWidget;
  private Widget currentWidget;
  private boolean goAhead = true;

  @Override
  public void setWidget(final IsWidget w) {
    Widget newWidget = asWidgetOrNull(w);
    if (newWidget == null) {
      return;
    }
    // Remove old child.
    if (previousWidget != null) {
      if (previousWidget == newWidget) {
        goAhead = false;
      }
      remove(previousWidget);
    }
    previousWidget = currentWidget;
    currentWidget = newWidget;
    add(newWidget);
  }

}
