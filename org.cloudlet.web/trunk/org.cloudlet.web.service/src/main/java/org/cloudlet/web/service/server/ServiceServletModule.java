package org.cloudlet.web.service.server;

import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.ServletModule;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.impl.BaseProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.EntityProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.ValueProxyCategory;

import org.cloudlet.web.service.server.jpa.DatabaseConnectionFilter;
import org.cloudlet.web.service.server.jpa.DatabaseConnectionProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ServiceServletModule extends ServletModule {
  @AutoBeanFactory.Category(value = {
      EntityProxyCategory.class, ValueProxyCategory.class, BaseProxyCategory.class})
  @AutoBeanFactory.NoWrap(EntityProxyId.class)
  interface Factory extends AutoBeanFactory {
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  private static final String SYMBOL_MAPS_DIRECTORY = "symbolMapsDirectory";

  @Override
  protected void configureServlets() {
    logger.finest("installPersistModule begin");

    Map<String, String> params = new HashMap<String, String>();
    // You'll need to compile with -extra and move the symbolMaps directory
    // to this location if you want stack trace deobfuscation to work
    params.put(SYMBOL_MAPS_DIRECTORY, "extra/web/symbolMaps/");
    serve("/gwtRequest").with(ServiceServlet.class, params);

    filter("/*").through(PersistFilter.class);

    requestStaticInjection(DatabaseConnectionProvider.class);
    filter("/*").through(DatabaseConnectionFilter.class);
    logger.finest("installPersistModule end");
  }

}
