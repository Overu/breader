package com.goodow.web.logging.server.ioc;

import com.goodow.web.logging.server.ChannelLoggingHandler;
import com.goodow.web.logging.server.servlet.ChannelPresenceSevlet;

import com.google.inject.servlet.ServletModule;


import java.util.HashMap;
import java.util.Map;

public class LoggingServletModule extends ServletModule {
  private static final String SYMBOL_MAPS_DIRECTORY = "symbolMapsDirectory";

  @Override
  protected void configureServlets() {
    Map<String, String> params = new HashMap<String, String>();
    // You'll need to compile with -extra and move the symbolMaps directory
    // to this location if you want stack trace deobfuscation to work
    params.put(SYMBOL_MAPS_DIRECTORY, "extra/web/symbolMaps/");
    serve("/_ah/channel/connected/", "/_ah/channel/disconnected/").with(
        ChannelPresenceSevlet.class, params);

    requestStaticInjection(ChannelLoggingHandler.class);
  }
}
