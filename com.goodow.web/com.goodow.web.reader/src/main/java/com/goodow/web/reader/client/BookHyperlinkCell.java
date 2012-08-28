package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.Book;
import com.goodow.web.reader.shared.ReaderPlace;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.inject.Inject;

public class BookHyperlinkCell extends AbstractCell<Book> {

  interface Template extends SafeHtmlTemplates {
    @Template("<a href=\"#/books/{0}/edit\" onclick=\"javascript: return false;\">{1}</a>")
    SafeHtml a(String value, String href);
  }

  @Inject
  PlaceController placeController;

  @Inject
  ReaderPlace reader;

  private Template template = GWT.create(Template.class);

  public BookHyperlinkCell() {
    super("click");
  }

  @Override
  public void onBrowserEvent(final Context context, final Element parent, final Book value,
      final NativeEvent event, final ValueUpdater<Book> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);
    if ("click".equals(event.getType())) {
      EventTarget eventTarget = event.getEventTarget();
      if (!Element.is(eventTarget)) {
        return;
      }
      if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget))) {
        // Ignore clicks that occur outside of the main element.
        onEnterKeyDown(context, parent, value, event, valueUpdater);
      }
    }
  }

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context, final Book value,
      final SafeHtmlBuilder sb) {
    sb.append(template.a(value.getId(), value.getTitle()));
  }

  @Override
  protected void onEnterKeyDown(final Context context, final Element parent, final Book value,
      final NativeEvent event, final ValueUpdater<Book> valueUpdater) {
    reader.booksPlace.bookPlace.setPath(value.getId());
    placeController.goTo(reader.booksPlace.bookPlace.editBookPlace);
  }
}
