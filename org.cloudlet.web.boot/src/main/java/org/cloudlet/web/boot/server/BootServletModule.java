package org.cloudlet.web.boot.server;

import com.google.inject.servlet.ServletModule;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class BootServletModule extends ServletModule {
  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configureServlets() {
    loadFromClasspath();
  }

  private void loadFromClasspath() {

    ServiceLoader<ServletModule> servletModules = ServiceLoader.load(ServletModule.class);
    Iterator<ServletModule> servletModuleItr = servletModules.iterator();
    while (servletModuleItr.hasNext()) {
      ServletModule servletModule = servletModuleItr.next();
      logger.finer("Install " + servletModule.getClass().getName());
      install(servletModule);
    }
  }
}
