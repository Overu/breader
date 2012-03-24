package org.cloudlet.web.logging.client;

import com.google.gwt.logging.client.HtmlLogFormatter;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.logging.LogRecord;

@Singleton
public final class LogFormatter extends HtmlLogFormatter {

  @Inject
  public LogFormatter() {
    super(false);
  }

  @Override
  public String format(LogRecord event) {
    StringBuilder html = new StringBuilder(getHtmlPrefix(event));
    html.append(getHtmlPrefix(event));
    html.append(event.getMessage());
    html.append(getHtmlSuffix(event));
    return html.toString();
  }

}
