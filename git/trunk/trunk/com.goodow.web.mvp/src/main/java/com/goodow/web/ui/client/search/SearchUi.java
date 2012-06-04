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
package com.goodow.web.ui.client.search;

import com.goodow.web.mvp.shared.ActivityAware;
import com.goodow.web.mvp.shared.ActivityState;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public final class SearchUi extends Composite implements ActivityAware {

  interface SearchUiBinder extends UiBinder<Widget, SearchUi> {
  }

  private static SearchUiBinder uiBinder = GWT.create(SearchUiBinder.class);

  @Inject
  SearchUi() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void onStart(final ActivityState state) {

  }
}
