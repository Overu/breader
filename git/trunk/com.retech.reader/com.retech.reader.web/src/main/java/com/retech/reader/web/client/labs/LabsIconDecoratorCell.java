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
package com.retech.reader.web.client.labs;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class LabsIconDecoratorCell extends AbstractCell<LabsIconDecorator> {

  public interface Delegate<ImageAndHyperlink> {
    void execute(ImageAndHyperlink object);
  }

  interface Template extends SafeHtmlTemplates {

    @SafeHtmlTemplates.Template("<div class='{2}'>{0}<span>{1}</span></div>")
    SafeHtml put(SafeHtml num, SafeHtml url, String style);
  }

  private static Template template = GWT.create(Template.class);
  private Delegate<LabsIconDecorator> delegate;

  public LabsIconDecoratorCell(final Delegate<LabsIconDecorator> delegate) {
    super(BrowserEvents.CLICK);
    this.delegate = delegate;
  }

  @Override
  public void onBrowserEvent(final com.google.gwt.cell.client.Cell.Context context,
      final Element parent, final LabsIconDecorator value, final NativeEvent event,
      final ValueUpdater<LabsIconDecorator> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);
    boolean isClick = event.getType().equals(BrowserEvents.CLICK);
    if (isClick) {
      onEnterKeyDown(context, parent, value, event, valueUpdater);
    }
  }

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context,
      final LabsIconDecorator value, final SafeHtmlBuilder sb) {
    if (value == null) {
      return;
    }
    SafeHtml image = AbstractImagePrototype.create(value.getImage()).getSafeHtml();
    SafeHtml link = SafeHtmlUtils.fromTrustedString(value.getTitle());
    sb.append(template.put(image, link, LabsResources.css().cellItemLeftDiv()));

  }

  @Override
  protected void onEnterKeyDown(final com.google.gwt.cell.client.Cell.Context context,
      final Element parent, final LabsIconDecorator value, final NativeEvent event,
      final ValueUpdater<LabsIconDecorator> valueUpdater) {
    if (value != null) {
      delegate.execute(value);
    }
  }
}
