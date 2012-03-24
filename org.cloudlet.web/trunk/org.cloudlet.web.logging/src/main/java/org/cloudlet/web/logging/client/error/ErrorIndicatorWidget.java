/**
 * Copyright 2010 Google Inc.
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
 * 
 */

package org.cloudlet.web.logging.client.error;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * GWT implementation of the UI for an error indicator.
 */
public final class ErrorIndicatorWidget extends Composite implements ErrorIndicatorView {

  interface Binder extends UiBinder<HTMLPanel, ErrorIndicatorWidget> {
  }

  interface Style extends CssResource {
    // Classes not used by code, but forced to be declared thanks to UiBinder.
    String detail();

    // Css classes used by code.
    String expanded();

    String stack();
  }

  private static final Binder BINDER = GWT.create(Binder.class);

  public static ErrorIndicatorWidget create() {
    return new ErrorIndicatorWidget();
  }

  @UiField
  Style style;
  @UiField
  Anchor showDetail;
  @UiField
  Element detail;
  @UiField
  Element stack;

  @UiField
  Element bug;

  private Listener listener;

  private ErrorIndicatorWidget() {
    initWidget(BINDER.createAndBindUi(this));
  }

  @Override
  public void collapseDetailBox() {
    detail.removeClassName(style.expanded());
  }

  @Override
  public void expandDetailBox() {
    detail.addClassName(style.expanded());
  }

  @Override
  public void hideDetailLink() {
    showDetail.setVisible(false);
  }

  @Override
  public void init(final Listener listener) {
    this.listener = listener;
  }

  @Override
  public void reset() {
    this.listener = null;
  }

  @Override
  public void setBug(final SafeHtml bug) {
    this.bug.setInnerHTML(bug.asString());
  }

  @Override
  public void setStack(final SafeHtml stack) {
    this.stack.setInnerHTML(stack.asString());
  }

  @Override
  public void showDetailLink() {
    showDetail.setVisible(true);
  }

  @UiHandler("showDetail")
  void handleClick(final ClickEvent e) {
    if (listener != null) {
      listener.onShowDetailClicked();
    }
  }
}
