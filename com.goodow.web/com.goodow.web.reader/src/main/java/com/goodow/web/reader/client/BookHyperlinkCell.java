package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class BookHyperlinkCell extends AbstractCell<Book> {

  interface Template extends SafeHtmlTemplates {
    @Template("<a href=\"#/books/id/{1}\">{0}</a>")
    SafeHtml a(String value, String href);
  }

  private Template template = GWT.create(Template.class);

  public BookHyperlinkCell() {
  }

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context, final Book value,
      final SafeHtmlBuilder sb) {
    sb.append(template.a(value.getTitle(), value.getId()));
  }
}
