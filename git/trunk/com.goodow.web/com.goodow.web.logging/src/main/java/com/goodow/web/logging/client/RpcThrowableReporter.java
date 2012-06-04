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
package com.goodow.web.logging.client;

import com.goodow.web.logging.shared.RpcLoggingService;
import com.goodow.web.logging.shared.RpcLoggingServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Singleton;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Singleton
final class RpcThrowableReporter {

  private static class RpcThrowableReporterHandler implements UncaughtExceptionHandler {
    private final RpcLoggingServiceAsync service = GWT.create(RpcLoggingService.class);

    @Override
    public void onUncaughtException(final Throwable e) {
      LogRecord lr = new LogRecord(Level.FINE, "前台程序出现未处理异常");
      lr.setThrown(e);
      AsyncCallback<LogRecord> callback = new AsyncCallback<LogRecord>() {

        @Override
        public void onFailure(final Throwable caught) {
          if (LogConfiguration.loggingIsEnabled()) {
            logger.log(Level.FINE, "解码前台异常时发生错误", caught);
          }
        }

        @Override
        public void onSuccess(final LogRecord result) {
          if (LogConfiguration.loggingIsEnabled()) {
            logger.log(result.getLevel(), result.getMessage(), result.getThrown());
          }
        }

      };
      service.deobfuscateLogRecord(lr, GWT.getPermutationStrongName(), GWT.getModuleName(),
          callback);
    }

  }

  private static final Logger logger = Logger.getLogger(RpcThrowableReporter.class.getName());

  public void run() {
    GWT.setUncaughtExceptionHandler(new RpcThrowableReporterHandler());
    logger.finer("开始捕获前台未处理异常");
  }
}
