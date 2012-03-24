package org.cloudlet.web.logging.server;

import java.util.logging.LogRecord;

final class HtmlLogFormatter extends com.google.gwt.logging.client.HtmlLogFormatter {

  public HtmlLogFormatter() {
    super(true);
  }

  @Override
  protected String getHtmlSuffix(LogRecord event) {
    return super.getHtmlSuffix(event) + "<br>";
  }
}
