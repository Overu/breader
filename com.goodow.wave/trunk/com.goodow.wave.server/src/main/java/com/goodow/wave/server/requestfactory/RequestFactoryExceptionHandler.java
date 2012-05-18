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
package com.goodow.wave.server.requestfactory;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

final class RequestFactoryExceptionHandler implements ExceptionHandler {
  private final Logger logger;

  @Inject
  RequestFactoryExceptionHandler(final Logger logger) {
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
