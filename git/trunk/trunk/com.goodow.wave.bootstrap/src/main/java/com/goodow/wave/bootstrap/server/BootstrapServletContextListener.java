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
package com.goodow.wave.bootstrap.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public final class BootstrapServletContextListener extends GuiceServletContextListener {

  private ServiceLoader<ServletContextListener> servletContextListeners;
  private static final Logger logger = Logger.getLogger(BootstrapServletContextListener.class
      .getName());

  @Override
  public void contextDestroyed(final ServletContextEvent servletContextEvent) {
    super.contextDestroyed(servletContextEvent);
    if (servletContextListeners != null) {
      Iterator<ServletContextListener> it = servletContextListeners.iterator();
      while (it.hasNext()) {
        ServletContextListener listener = it.next();
        logger.finer("Destroy ServletContextListener: " + listener.getClass().getName());
        listener.contextDestroyed(servletContextEvent);
      }
    }
    logger.finer("Destroyed ServletContextListener");
  }

  @Override
  public void contextInitialized(final ServletContextEvent servletContextEvent) {
    BootstrapUtil.restoreSystemProperties();
    if (servletContextListeners == null) {
      servletContextListeners = ServiceLoader.load(ServletContextListener.class);
    }
    if (servletContextListeners != null) {
      Iterator<ServletContextListener> it = servletContextListeners.iterator();
      while (it.hasNext()) {
        ServletContextListener listener = it.next();
        logger.finer("Initialize ServletContextListener: " + listener.getClass().getName());
        listener.contextInitialized(servletContextEvent);
      }
    }
    super.contextInitialized(servletContextEvent);
    logger.finer("Initialized ServletContextListener");
  }

  @Override
  protected Injector getInjector() {
    Injector injector = Guice.createInjector(new BootstrapModule(), new BootstrapServletModule());
    return injector;
  }
}
