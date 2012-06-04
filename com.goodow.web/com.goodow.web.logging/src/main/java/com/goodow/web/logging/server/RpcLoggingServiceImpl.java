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
package com.goodow.web.logging.server;

import com.goodow.web.logging.shared.RpcLoggingService;

import com.google.gwt.logging.server.StackTraceDeobfuscator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.logging.LogRecord;

final class RpcLoggingServiceImpl implements RpcLoggingService {

  protected static Log logger = LogFactory.getLog(RpcLoggingServiceImpl.class);

  private static String SYMBOLMAPS_DIRECTORY;
  private static StackTraceDeobfuscator stackTraceDeobfuscator = new StackTraceDeobfuscator(
      SYMBOLMAPS_DIRECTORY);

  @Override
  public LogRecord deobfuscateLogRecord(final LogRecord lr, final String strongName,
      final String moduleName) {
    if (SYMBOLMAPS_DIRECTORY == null) {
      String warRootPath = System.getProperty("warRootPath");
      SYMBOLMAPS_DIRECTORY = warRootPath + "stat/gwt/extra/" + moduleName + "/symbolMaps/";
      stackTraceDeobfuscator.setSymbolMapsDirectory(SYMBOLMAPS_DIRECTORY);
      logger.info("gwt symbolMaps directory location: " + SYMBOLMAPS_DIRECTORY);
    }
    LogRecord logRecord = stackTraceDeobfuscator.deobfuscateLogRecord(lr, strongName);
    return logRecord;
  }

}