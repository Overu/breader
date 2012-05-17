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
package com.goodow.wave.test;

import java.net.URL;
import java.util.logging.FileHandler;

public class LoggingUtil {

  static final String LOGGING_CONFIG_FILE = "/logging.properties";
  static final String FILE_PATTERN = FileHandler.class.getName() + ".pattern";

  public static URL searchLoggingFile() {
    URL url = null;
    url = LoggingUtil.class.getResource(LOGGING_CONFIG_FILE);
    if (url == null) {
      System.err.println("can't find logging.properties in classpath:" + LOGGING_CONFIG_FILE);
    }
    return url;
  }
}
