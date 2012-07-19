package com.goodow.web.reader.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import com.googlecode.mgwt.ui.client.widget.celllist.Cell;

public abstract class BookCell<T> implements Cell<T> {

  public interface Template extends SafeHtmlTemplates {
    @SafeHtmlTemplates.Template("<div class=\"{0}\">{1}</div>")
    SafeHtml content(String classes, String cellContents);

  }

  private static Template TEMPLATE = GWT.create(Template.class);

  @Override
  public boolean canBeSelected(final T model) {
    return true;
  }

  public abstract String getDisplayString(T model);

  @Override
  public void render(final SafeHtmlBuilder safeHtmlBuilder, final T model) {
    safeHtmlBuilder.append(TEMPLATE.content("", SafeHtmlUtils.htmlEscape(getDisplayString(model))));

  }

}