package com.goodow.web.core.shared;

import java.io.Serializable;

public class FailureMessage implements Serializable {

  public static final String EXCEPTION_TYPE = "X";

  public static final String MESSAGE = "M";

  public static final String STACK_TRACE = "S";

  public static final String FATAL = "F";

  private String exceptionType;

  private String message;

  private String stackTrace;

  private boolean fatal;

  public String getExceptionType() {
    return exceptionType;
  }

  public String getMessage() {
    return message;
  }

  public String getStackTrace() {
    return stackTrace;
  }

  public boolean isFatal() {
    return fatal;
  }

  public void setExceptionType(final String exceptionType) {
    this.exceptionType = exceptionType;
  }

  public void setFatal(final boolean significant) {
    this.fatal = significant;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public void setStackTrace(final String stackTrace) {
    this.stackTrace = stackTrace;
  }
}
