package org.cloudlet.web.service.server;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

final class ServiceExceptionHandler implements ExceptionHandler {
  private final Logger logger;

  @Inject
  ServiceExceptionHandler(final Logger logger) {
    this.logger = logger;
  }

  @Override
  public ServerFailure createServerFailure(final Throwable throwable) {
    // if (throwable instanceof InvocationTargetException) {
    // throwable = ((InvocationTargetException) throwable).getTargetException();
    // }
    logger.log(Level.SEVERE, "远程调用异常", throwable);
    String stackTraceString = null;
    if (throwable != null) {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      throwable.printStackTrace(printWriter);
      stackTraceString = stringWriter.toString();
    }
    return new ServerFailure(
        "Server Error: " + (throwable == null ? null : throwable.getMessage()), null,
        stackTraceString, true);
  }

}
