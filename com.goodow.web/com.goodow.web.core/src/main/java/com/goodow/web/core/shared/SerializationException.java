package com.goodow.web.core.shared;

public class SerializationException extends WebException {
  public SerializationException() {
  }

  public SerializationException(final String msg) {
    super(msg);
  }

  public SerializationException(final String msg, final Throwable cause) {
    super(msg, cause);
  }

  public SerializationException(final Throwable cause) {
    super(cause);
  }
}
