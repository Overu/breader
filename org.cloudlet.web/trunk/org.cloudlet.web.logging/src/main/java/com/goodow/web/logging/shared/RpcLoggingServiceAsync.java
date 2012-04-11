package com.goodow.web.logging.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.logging.LogRecord;

public interface RpcLoggingServiceAsync {

  void deobfuscateLogRecord(LogRecord lr, String strongName, String moduleName,
      AsyncCallback<LogRecord> callback);

}
