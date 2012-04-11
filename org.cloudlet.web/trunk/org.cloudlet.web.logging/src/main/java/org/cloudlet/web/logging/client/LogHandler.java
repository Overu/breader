package org.cloudlet.web.logging.client;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

@Singleton
public final class LogHandler extends Handler {
  private PopupPanel popup;
  private HTML html = new HTML();
  private Level currentLevel = Level.ALL;

  @Inject
  public LogHandler(final LogFormatter formatter) {
    setFormatter(formatter);
    setLevel(Level.INFO);

    popup = new PopupPanel(true);
    popup.setPreviewingAllNativeEvents(true);
    popup.setWidget(html);
  }

  @Override
  public void close() {
    // Do nothing
  }

  @Override
  public void flush() {
    // Do nothing
  }

  @Override
  public void publish(final LogRecord record) {
    boolean requestHide = record.getMessage() == null && Level.INFO.equals(record.getLevel());
    if (!isLoggable(record) && !requestHide) {
      return;
    }
    if (popup.isShowing() && currentLevel.equals(Level.SEVERE)) {
      return;
    }
    if (requestHide) {
      popup.hide();
      return;
    }
    // if (popup.isShowing() && currentLevel.intValue() > record.getLevel().intValue()) {
    // return;
    // }

    if (record.getLevel().intValue() >= Level.SEVERE.intValue()) {
      // popup.setAutoHideEnabled(false);
    }
    if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
      popup.setGlassEnabled(true);
    } else {
      popup.setGlassEnabled(false);
    }
    currentLevel = record.getLevel();

    Formatter formatter = getFormatter();
    String msg = formatter.format(record);
    // We want to make sure that unescaped messages are not output as HTML to
    // the window and the HtmlLogFormatter ensures this. If you want to write a
    // new formatter, subclass HtmlLogFormatter and override the getHtmlPrefix
    // and getHtmlSuffix methods.
    html.setHTML(msg);
    show();
  }

  private void show() {
    popup.hide();
    popup.center();
    popup.setPopupPosition(popup.getPopupLeft(), 0);
    popup.show();
    popup.getElement().getStyle().setPosition(Position.FIXED);
  }
}
