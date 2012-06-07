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
package com.goodow.web.logging.client.ioc;

import com.goodow.web.logging.client.LogHandler;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingGinModule extends AbstractGinModule {
  @Singleton
  public static class Binder {

    private static final Logger wireLogger = Logger.getLogger("WireActivityLogger");

    @Inject
    public Binder(final LogHandler logHandler
    // , final ChannelContextProvider channelContextProvider
    // , final LoggingRequestProvider loggingRequestProvider
    ) {
      logger.finest("EagerSingleton start");

      Logger rootLogger = Logger.getLogger("");
      rootLogger.addHandler(logHandler);
      ArrayList<String> ignoredLoggerNames = new ArrayList<String>();
      // ignoredLoggerNames.add(LoggingGinModule.class.getName());

      // rootLogger.addHandler(new RequestFactoryLogHandler(loggingRequestProvider, Level.WARNING,
      // ignoredLoggerNames));

      // if (GWT.isProdMode() && Connectivity.isOnline()) {
      // if (false) {
      // logger.finest("request token");
      // channelContextProvider.channelContext().getToken("logging." + new Date().toString()).fire(
      // new Receiver<String>() {
      //
      // @Override
      // public void onSuccess(final String response) {
      // openChannel(response);
      // }
      // });
      // }

      logger.finest("EagerSingleton end");
    }

    private void openChannel(final String token) {
      logger.config("openChannel:" + token);
      ChannelFactory.createChannel(token, new ChannelCreatedCallback() {
        @Override
        public void onChannelCreated(final Channel channel) {
          channel.open(new SocketListener() {
            @Override
            public void onClose() {
              wireLogger.log(Level.WARNING, "你掉线了");
            }

            @Override
            public void onError(final SocketError error) {
              wireLogger.log(Level.SEVERE, error.getDescription());
            }

            @Override
            public void onMessage(final String message) {
              wireLogger.log(Level.INFO, message);
            }

            @Override
            public void onOpen() {
              wireLogger.log(Level.INFO, "上线成功");
            }
          });
        }
      });
    }
  }

  private static final Logger logger = Logger.getLogger(LoggingGinModule.class.getName());

  @Override
  protected void configure() {
    bind(Binder.class).asEagerSingleton();
  }

}
