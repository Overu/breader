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

import com.google.inject.servlet.ServletModule;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class BootstrapServletModule extends ServletModule {
  private static final Logger logger = Logger.getLogger(BootstrapServletModule.class.getName());

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
