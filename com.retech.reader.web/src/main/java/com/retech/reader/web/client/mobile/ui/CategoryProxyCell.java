package com.retech.reader.web.client.mobile.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import com.retech.reader.web.shared.proxy.CategoryProxy;

public class CategoryProxyCell extends AbstractCell<CategoryProxy> {

  interface Template extends SafeHtmlTemplates {

    @SafeHtmlTemplates.Template("{0}")
    SafeHtml info(SafeHtml name);
  }

  private static Template template;

  public CategoryProxyCell() {
    if (template == null) {
      template = GWT.create(Template.class);
    }
  }

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context,
      final CategoryProxy value, final SafeHtmlBuilder sb) {
    if (value == null) {
      return;
    }

    SafeHtml name = null;
    if (value.getTitle() != null) {
      name = SafeHtmlUtils.fromString(value.getTitle());
    }
    sb.append(template.info(name));
  }
}
