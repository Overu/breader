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

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class TrangleButtonCell<T> extends AbstractEditableCell<T, Boolean> {

  public static interface Delegate<T> {
    void execute(T object);
  }

  interface Bundle extends ClientBundle {
    ImageResource trangleButton();

    ImageResource trangleButtoned();
  }

  private static Bundle bundle = GWT.create(Bundle.class);

  private static SafeHtml TRANGLE_BUTTON = AbstractImagePrototype.create(bundle.trangleButton())
      .getSafeHtml();
  private static SafeHtml TRANGLE_BUTTONED = AbstractImagePrototype
      .create(bundle.trangleButtoned()).getSafeHtml();

  private boolean isClick = false;
  private TrangeleComboBoxPopupPanel pp;

  private Delegate<T> delegate = new TrangleButtonCell.Delegate<T>() {

    @Override
    public void execute(final T object) {
      if (pp == null) {
        pp = new TrangeleComboBoxPopupPanel();
      }
      if (isClick) {
        pp.show();
        Style style = pp.getElement().getStyle();
        style.setTop(imageElm.getAbsoluteTop() + 15, Unit.PX);
        style.setLeft(imageElm.getAbsoluteLeft() - pp.getOffsetWidth(), Unit.PX);
      } else {
        pp.hide();
      }
    }
  };

  private Element imageElm;

  public TrangleButtonCell() {
    super(BrowserEvents.CLICK, BrowserEvents.BLUR);
  }

  @Override
  public boolean isEditing(final com.google.gwt.cell.client.Cell.Context context,
      final Element parent, final T value) {
    return false;
  }

  @Override
  public void onBrowserEvent(final com.google.gwt.cell.client.Cell.Context context,
      final Element parent, final T value, final NativeEvent event,
      final ValueUpdater<T> valueUpdater) {
    boolean clickEvent = event.getType().equals(BrowserEvents.CLICK);
    boolean blurEvent = event.getType().equals(BrowserEvents.BLUR);
    imageElm = parent;
    if (clickEvent || blurEvent) {
      EventTarget eventTarget = event.getEventTarget();
      if (!Element.is(eventTarget)) {
        return;
      }

      if (blurEvent) {
        isClick = false;
        setValue(context, parent, value);
        onEnterKeyDown(context, parent, value, event, valueUpdater);
        parent.removeAttribute("tabindex");
      }

      if (parent.isOrHasChild(Element.as(eventTarget))) {
        if (clickEvent) {
          if (!isClick) {
            isClick = true;
          } else {
            isClick = false;
          }
          imageElm.getParentElement().removeAttribute("tabindex");
          imageElm.setTabIndex(1);
          parent.focus();
          // parent.setAttribute("tabindex", "0");
          setValue(context, parent, value);
          onEnterKeyDown(context, parent, value, event, valueUpdater);
        }
      }

    }
  }

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context, final T value,
      final SafeHtmlBuilder sb) {

    if (value != null && !isClick) {
      // sb.append(template.div(TRANGLE_BUTTON));
      // sb.append(SafeHtmlUtils.fromTrustedString("<div>"));
      sb.append(TRANGLE_BUTTON);
      // sb.append(SafeHtmlUtils.fromTrustedString("</div>"));
    } else {
      // sb.append(template.div(TRANGLE_BUTTONED));
      // sb.append(SafeHtmlUtils.fromTrustedString("<div>"));
      sb.append(TRANGLE_BUTTONED);
      // sb.append(SafeHtmlUtils.fromTrustedString("</div>"));
    }
  }

  @Override
  protected void onEnterKeyDown(final com.google.gwt.cell.client.Cell.Context context,
      final Element parent, final T value, final NativeEvent event,
      final ValueUpdater<T> valueUpdater) {
    delegate.execute(value);
  }

}
