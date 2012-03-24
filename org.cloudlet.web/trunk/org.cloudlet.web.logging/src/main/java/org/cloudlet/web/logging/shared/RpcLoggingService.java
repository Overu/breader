package org.cloudlet.web.logging.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.logging.LogRecord;

@RemoteServiceRelativePath("RpcLoggingService.gwt")
public interface RpcLoggingService extends RemoteService {
  LogRecord deobfuscateLogRecord(LogRecord lr, String strongName, String moduleName);
}
