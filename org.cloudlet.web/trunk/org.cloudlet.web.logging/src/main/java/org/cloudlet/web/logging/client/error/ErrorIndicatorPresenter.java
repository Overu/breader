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

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Panel;

/**
 * Presents an error message, with asynchronous addition of detail.
 */
public final class ErrorIndicatorPresenter implements ErrorIndicatorView.Listener {

  /**
   * Creates an error indicator.
   * 
   * @param container panel to which this widget is to be attached
   */
  public static ErrorIndicatorPresenter create(final Panel container) {
    ErrorIndicatorWidget ui = ErrorIndicatorWidget.create();
    ErrorIndicatorPresenter presenter = new ErrorIndicatorPresenter(ui);
    ui.init(presenter);
    container.add(ui);
    return presenter;
  }

  /** View controlled by this presenter. */
  private final ErrorIndicatorView ui;

  private ErrorIndicatorPresenter(final ErrorIndicatorView ui) {
    this.ui = ui;
  }

  /**
   * Adds extra detail about the error to the indicator.
   * 
   * @param stack a stack trace
   * @param bug a bug report
   */
  public void addDetail(final SafeHtml stack, final SafeHtml bug) {
    ui.setStack(stack);
    if (bug != null) {
      ui.setBug(bug);
    }
    ui.showDetailLink();
  }

  @Override
  public void onShowDetailClicked() {
    ui.hideDetailLink();
    ui.expandDetailBox();
  }
}
