package com.retech.reader.web.client.mobile.ui;

import com.goodow.wave.client.widget.button.icon.IconButtonTemplate.Resources;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import com.retech.reader.web.shared.proxy.CategoryProxy;

public class CategoryProxyCell extends AbstractCell<CategoryProxy> {

  interface Template extends SafeHtmlTemplates {

    @SafeHtmlTemplates.Template("<div class='{1}' style='display: inline-block; margin-left: 12px;'>{0}</div>")
    SafeHtml count(SafeHtml num, String style);

    @SafeHtmlTemplates.Template("<div style='display: inline-block;'>{0}{1}</div>")
    SafeHtml info(SafeHtml name, SafeHtml num);

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
    SafeHtml count = null;
    if (value.getTitle() != null) {
      name = SafeHtmlUtils.fromString(value.getTitle());
      count = SafeHtmlUtils.fromString(String.valueOf(value.getCount()));
    }
    sb.append(template.info(name, template.count(count, Resources.INSTANCE.style()
        .visualNumeralElement())));
  }
}
