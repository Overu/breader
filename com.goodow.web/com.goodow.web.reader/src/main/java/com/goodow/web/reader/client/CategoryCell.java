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
package com.goodow.web.reader.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import com.googlecode.mgwt.ui.client.widget.celllist.Cell;

public class CategoryCell implements Cell<Category> {

  interface Template extends SafeHtmlTemplates {
    @SafeHtmlTemplates.Template("<div><div>{0}</div><div>{1}本</div></div>")
    SafeHtml content(String name, int count);

  }

  private static Template TEMPLATE = GWT.create(Template.class);

  @Override
  public boolean canBeSelected(final Category model) {
    return true;
  }

  @Override
  public void render(final SafeHtmlBuilder safeHtmlBuilder, final Category model) {
    safeHtmlBuilder.append(TEMPLATE.content(model.getName(), model.getBookCount()));
  }
}
