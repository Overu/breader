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

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class ImageCell extends AbstractCell<String> {

  interface Template extends SafeHtmlTemplates {
    @Template("<img style=\"width: 80px; height: 100px\" src=\"{0}\"/>")
    SafeHtml img(String url);
  }

  private static Template template;

  public ImageCell() {
    if (template == null) {
      template = GWT.create(Template.class);
    }
  }

  @Override
  public void render(final Context context, final String value, final SafeHtmlBuilder sb) {
    if (value != null) {
      sb.append(template.img(value));
    }
  }

}
