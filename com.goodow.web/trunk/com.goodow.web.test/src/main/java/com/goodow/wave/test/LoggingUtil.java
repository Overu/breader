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
