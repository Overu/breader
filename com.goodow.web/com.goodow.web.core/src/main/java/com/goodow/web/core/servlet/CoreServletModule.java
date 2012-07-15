package com.goodow.web.core.servlet;

import com.goodow.web.core.server.DatabaseConnectionProvider;
import com.goodow.web.core.server.OpenIdAuthServlet;
import com.goodow.web.core.server.ShiroSecurityModule;

import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.ServletModule;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.impl.BaseProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.EntityProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.ValueProxyCategory;

import org.apache.shiro.guice.web.GuiceShiroFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CoreServletModule extends ServletModule {
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

    serveRegex("/\\w+/" + MediaUploadServlet.END_POINT).with(MediaUploadServlet.class);

    requestStaticInjection(DatabaseConnectionProvider.class);
    filter("/*").through(DatabaseConnectionFilter.class);

    filter("/*").through(GuiceShiroFilter.class);
    serve("/_ah/login_required").with(OpenIdAuthServlet.class);

    install(new ShiroSecurityModule(getServletContext()));
    logger.finest("installPersistModule end");
  }

}
