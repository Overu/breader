package com.goodow.web.reader.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
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
