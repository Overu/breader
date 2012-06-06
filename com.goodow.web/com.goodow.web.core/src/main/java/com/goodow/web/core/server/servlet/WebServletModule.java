package com.goodow.web.core.server.servlet;

import com.goodow.web.core.server.DatabaseConnectionFilter;
import com.goodow.web.core.server.DatabaseConnectionProvider;
import com.goodow.web.core.server.WebServiceServlet;

import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.ServletModule;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.impl.BaseProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.EntityProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.ValueProxyCategory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class WebServletModule extends ServletModule {
  @AutoBeanFactory.Category(value = {
      EntityProxyCategory.class, ValueProxyCategory.class, BaseProxyCategory.class})
  @AutoBeanFactory.NoWrap(EntityProxyId.class)
  interface Factory extends AutoBeanFactory {
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configureServlets() {
    logger.finest("installPersistModule begin");
    filter("/*").through(PersistFilter.class);
    Map<String, String> params = new HashMap<String, String>();
    // serveRegex("/\\w+/" + RequestProcessor.END_POINT).with(RequestProcessorImpl.class);
    // serve("/gwtRequest").with(ServiceDispatcherImpl.class, params);

    serveRegex("/\\w+/" + WebServiceServlet.END_POINT).with(WebServiceServlet.class);

    requestStaticInjection(DatabaseConnectionProvider.class);
    filter("/*").through(DatabaseConnectionFilter.class);

    logger.finest("installPersistModule end");
  }

}
