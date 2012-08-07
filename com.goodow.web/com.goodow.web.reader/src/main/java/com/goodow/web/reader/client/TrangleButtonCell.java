package com.goodow.web.reader.client;

import com.goodow.web.reader.client.style.ReadResources;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class TrangleButtonCell<T> extends AbstractCell<T> {

  public static interface Delegate<T> {
    void execute(T object);
  }

  interface Bundle extends ClientBundle {
    ImageResource trangleButton();

    ImageResource trangleButtoned();
  }

  interface Template extends SafeHtmlTemplates {

    @SafeHtmlTemplates.Template("<div class='{0}' tabIndex='0' selectignore='1'></div>")
    SafeHtml div(String style);
  }

  private static Template template = GWT.create(Template.class);

  private boolean isClick = false;

  private Delegate<T> delegate = new TrangleButtonCell.Delegate<T>() {

    @Override
    public void execute(final T object) {

    }
  };

  // private Element imageElm;

  public TrangleButtonCell() {
    super(BrowserEvents.CLICK, BrowserEvents.BLUR);
  }

  @Override
  public boolean isEditing(final com.google.gwt.cell.client.Cell.Context context,
      final Element parent, final T value) {
    return false;
  }

  @Override
  public void onBrowserEvent(final com.google.gwt.cell.client.Cell.Context context,
      final Element parent, final T value, final NativeEvent event,
      final ValueUpdater<T> valueUpdater) {
    boolean clickEvent = event.getType().equals(BrowserEvents.CLICK);
    boolean blurEvent = event.getType().equals(BrowserEvents.BLUR);
    // imageElm = parent;
    if (clickEvent || blurEvent) {
      EventTarget eventTarget = event.getEventTarget();
      if (!Element.is(eventTarget)) {
        return;
      }

      if (parent.isOrHasChild(Element.as(eventTarget))) {
        if (!isClick) {
          isClick = true;
          parent.focus();
          onEnterKeyDown(context, parent, value, event, valueUpdater);
        } else {
          parent.blur();
          isClick = false;
        }
      }
    }
  }

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context, final T value,
      final SafeHtmlBuilder sb) {

    sb.append(template.div(ReadResources.INSTANCE().css().trangleCell()));
  }

  @Override
  protected void onEnterKeyDown(final com.google.gwt.cell.client.Cell.Context context,
      final Element parent, final T value, final NativeEvent event,
      final ValueUpdater<T> valueUpdater) {
    delegate.execute(value);
  }

}
