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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

final class BootstrapUtil {

  private static final Logger logger = Logger.getLogger(BootstrapUtil.class.getName());
  private static final String SYSTEM_PROPERTIES = "META-INF/cfg/system.properties";

  static void restoreSystemProperties() {
    Properties props = loadPropertiesFromClasspath(SYSTEM_PROPERTIES);
    for (Map.Entry<Object, Object> entry : props.entrySet()) {
      String key = entry.getKey().toString();
      String value = entry.getValue().toString();
      if (System.getProperties().containsKey(key)) {
        String originalValue = System.getProperty(key);
        System.clearProperty(key);
        logger.log(Level.INFO, "override system property " + key + " from " + originalValue
            + " to " + value);
      } else {
        logger.log(Level.FINER, "set system property " + key + " = " + value);
      }
      System.setProperty(key, value);
    }
  }

  /**
   * Loads all properties from classpath.
   */
  private static Properties loadPropertiesFromClasspath(final String path) {
    Enumeration<URL> locations;
    Properties props = new Properties();
    try {
      locations = Thread.currentThread().getContextClassLoader().getResources(path);
      while (locations.hasMoreElements()) {
        URL url = locations.nextElement();
        InputStream in = url.openStream();
        props.load(in);
        in.close();
        logger.config("Load properties from " + url);
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "load properties from classpath \"" + path + "\" failed", e);
    }
    return props;
  }
}
