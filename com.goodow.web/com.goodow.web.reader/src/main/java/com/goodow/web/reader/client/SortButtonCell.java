package com.goodow.web.reader.client;

import com.goodow.web.reader.client.style.ReadResources;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;

public abstract class SortButtonCell<T> extends AbstractCell<T> {

  public static interface Delegate<T> {
    void execute(T object, Column<?, ?> column, Element curElm);
  }

  public interface Template extends SafeHtmlTemplates {
    @SafeHtmlTemplates.Template("<div style=\"display: inline-block;\">{0}</div><div class=\"{1}\"></div>")
    SafeHtml content(String name, String className);
  }

  private static Template template = GWT.create(Template.class);
  private final Delegate<T> delegate;
  private Column<?, ?> column;

  public SortButtonCell(final Delegate<T> delegate, final Column<?, ?> column) {
    super(BrowserEvents.CLICK);
    this.delegate = delegate;
    this.column = column;
  }

  public abstract String getValue(T value);

  @Override
  public void onBrowserEvent(final Context context, final Element parent, final T value,
      final NativeEvent event, final ValueUpdater<T> valueUpdater) {
    if (BrowserEvents.CLICK.equals(event.getType())) {
      EventTarget eventTarget = event.getEventTarget();
      if (!Element.is(eventTarget)) {
        return;
      }
      Element elm = eventTarget.cast();
      if (elm.getClassName().contains(ReadResources.INSTANCE().css().sortButtonCell())) {
        onEnterKeyDown(context, elm, value, event, valueUpdater);
      }
    }
  }

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context, final T value,
      final SafeHtmlBuilder sb) {
    sb.append(template.content(getValue(value), ReadResources.INSTANCE().css().sortButtonCell()));
  }

  @Override
  protected void onEnterKeyDown(final Context context, final Element parent, final T value,
      final NativeEvent event, final ValueUpdater<T> valueUpdater) {
    delegate.execute(value, column, parent);
  }
}
