package org.cloudlet.web.logging.server;

import com.google.gwt.logging.server.StackTraceDeobfuscator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cloudlet.web.logging.shared.RpcLoggingService;

import java.util.logging.LogRecord;

final class RpcLoggingServiceImpl implements RpcLoggingService {

  protected static Log logger = LogFactory.getLog(RpcLoggingServiceImpl.class);

  private static String SYMBOLMAPS_DIRECTORY;
  private static StackTraceDeobfuscator stackTraceDeobfuscator = new StackTraceDeobfuscator(
      SYMBOLMAPS_DIRECTORY);

  @Override
  public LogRecord deobfuscateLogRecord(LogRecord lr, String strongName, String moduleName) {
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
