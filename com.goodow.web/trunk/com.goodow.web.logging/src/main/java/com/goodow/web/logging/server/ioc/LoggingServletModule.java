/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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
