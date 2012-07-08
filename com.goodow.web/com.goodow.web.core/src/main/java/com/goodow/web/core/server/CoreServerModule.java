package com.goodow.web.core.server;

import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.RequestProcessor;
import com.goodow.web.core.shared.WebPlatform;

import com.google.inject.AbstractModule;

import java.util.logging.Logger;

public class CoreServerModule extends AbstractModule {

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    logger.finest("Install JpaServiceModule begin");
    requestStaticInjection(WebPlatform.class);
    bind(Message.class).toProvider(MessageProvider.class);
    bind(RequestProcessor.class).to(ServerRequestProcessor.class);
    logger.finest("Install JpaServiceModule end");
  }

}
