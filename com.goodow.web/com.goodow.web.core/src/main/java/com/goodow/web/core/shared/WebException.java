package com.goodow.web.core.shared;

@SuppressWarnings("serial")
public class WebException extends RuntimeException {

  public WebException() {
  }

  public WebException(final String message) {
    this(message, null);
  }

  public WebException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public WebException(final Throwable cause) {
    super(cause);
  }
}
