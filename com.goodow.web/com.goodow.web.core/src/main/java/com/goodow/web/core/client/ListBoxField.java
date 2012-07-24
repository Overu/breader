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
package com.goodow.web.core.client;

import com.goodow.web.core.shared.Category;

import com.google.gwt.user.client.ui.ListBox;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class ListBoxField extends FormField<Category> {

  @Inject
  ListBox listBox;

  List<Category> categorys;

  public void clear() {
    if (listBox != null && categorys != null) {
      listBox.clear();
      categorys.clear();
    }
  }

  @Override
  public Category getValue() {
    if (categorys != null) {
      return categorys.get(listBox.getTabIndex());
    }
    return null;
  }

  @Override
  public void setValue(final Category value) {
    if (value == null) {
      return;
    }

    listBox.addItem(value.getTitle());
    if (categorys == null) {
      categorys = new ArrayList<Category>();
    }

    categorys.add(value);
  }

  @Override
  protected void start() {
    super.start();
    main.add(listBox);
  }

}
