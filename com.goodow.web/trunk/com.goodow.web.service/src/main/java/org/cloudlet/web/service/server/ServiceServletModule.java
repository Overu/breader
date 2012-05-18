package org.cloudlet.web.service.server;

import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.ServletModule;

import org.cloudlet.web.service.server.jpa.DatabaseConnectionFilter;
import org.cloudlet.web.service.server.jpa.DatabaseConnectionProvider;

import java.util.logging.Logger;

public class ServiceServletModule extends ServletModule {

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configureServlets() {
    logger.finest("installPersistModule begin");

    filter("/*").through(PersistFilter.class);

    requestStaticInjection(DatabaseConnectionProvider.class);
    filter("/*").through(DatabaseConnectionFilter.class);
    logger.finest("installPersistModule end");
  }

}
