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

import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.rebind.adapter.GinModuleAdapter;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public final class BootstrapModule extends AbstractModule {

  private static final Logger logger = Logger.getLogger(BootstrapModule.class.getName());
  public static final String INT_PROPERTIES = "/ini.properties";
  public static Properties props = new Properties();

  @Override
  protected void configure() {
    try {
      URL url = BootstrapModule.class.getResource(INT_PROPERTIES);
      InputStream in = url.openStream();
      props.load(in);
      logger.config("load init properties " + url);
      Names.bindProperties(binder(), props);
    } catch (Exception e) {
      logger.finest("classpath:/init.properties not exist, use out-of-box values");
      // okay... we use default values, then.
    }

    loadFromClasspath();
  }

  private void loadFromClasspath() {
    ServiceLoader<Module> modules = ServiceLoader.load(Module.class);
    Iterator<Module> moduleIt = modules.iterator();
    while (moduleIt.hasNext()) {
      Module module = moduleIt.next();
      logger.finer("Install " + module.getClass().getName());
      install(module);
    }

    ServiceLoader<GinModule> ginModules = ServiceLoader.load(GinModule.class);
    Iterator<GinModule> ginModuleItr = ginModules.iterator();
    while (ginModuleItr.hasNext()) {
      GinModule ginModule = ginModuleItr.next();
      GinModuleAdapter module = new GinModuleAdapter(ginModule);
      logger.finer("Install " + ginModule.getClass().getName());
      install(module);
    }
  }
}
