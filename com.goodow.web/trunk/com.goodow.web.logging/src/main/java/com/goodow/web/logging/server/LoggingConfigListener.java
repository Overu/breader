package com.goodow.web.logging.server;

// package com.goodow.web.logging.server;
//
// import static com.goodow.web.logging.server.LoggingUtil.FILE_PATTERN;
//
// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;
// import java.io.File;
// import java.io.IOException;
// import java.io.InputStream;
// import java.net.URL;
// import java.util.Properties;
// import java.util.logging.LogManager;
// import java.util.logging.Logger;
//
// import javax.servlet.ServletContext;
// import javax.servlet.ServletContextEvent;
// import javax.servlet.ServletContextListener;
//
// public final class LoggingConfigListener implements ServletContextListener {
//
// private final Logger logger = Logger.getLogger(getClass().getName());
// private ServletContext servletContext;
//
// @Override
// public void contextDestroyed(ServletContextEvent arg0) {
// logger.finest("contextDestroyed");
// }
//
// @Override
// public void contextInitialized(ServletContextEvent servletContextEvent) {
// logger.finest("contextInitialized");
// servletContext = servletContextEvent.getServletContext();
// if (servletContext.getServerInfo().toLowerCase().contains("tomcat")) {
// logger.config("use tomcat's build-in logging configuration: "
// + LoggingUtil.LOGGING_CONFIG_FILE);
// return;
// }
//
// Properties props = new Properties();
// URL url = LoggingUtil.searchLoggingFile();
// if (url == null) {
// return;
// }
//
// LogManager logManager = LogManager.getLogManager();
//
// InputStream in;
// try {
// in = url.openStream();
//
// props.load(in);
//
// boolean success = processFilePattern(props);
//
// if (success) {
// ByteArrayOutputStream out = new ByteArrayOutputStream();
// props.store(out, null);
// logManager.readConfiguration(new ByteArrayInputStream(out.toByteArray()));
// } else {
// logManager.readConfiguration(in);
// }
// if (in != null) {
// in.close();
// }
// } catch (IOException e) {
// System.err.println("process logging configuration failed");
// System.err.println("" + e);
// }
//
// logger.config("Config logging use: " + url);
// }
//
// private boolean processFilePattern(Properties props) {
// String pattern = props.getProperty(FILE_PATTERN);
// if (pattern != null && pattern.toLowerCase().contains("catalina")) {
// String webAppDir = servletContext.getRealPath("/");
// pattern = webAppDir + "logs/log%g.html";
// int lastIndexOf = pattern.lastIndexOf("/");
// String folder = pattern.substring(0, lastIndexOf + 1);
// File file = new File(folder);
// if (!file.exists()) {
// System.out.println("folder " + file + " not exist, create automatically");
// file.mkdirs();
// }
// props.setProperty(FILE_PATTERN, pattern);
// System.out.println("log file location: " + pattern);
// return true;
// }
// return false;
// }
// }
