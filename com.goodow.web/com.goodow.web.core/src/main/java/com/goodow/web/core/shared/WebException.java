package com.goodow.web.core.shared;

@SuppressWarnings("serial")
public class WebException extends RuntimeException {

  private HttpStatus status;

  public WebException() {
  }

  public WebException(HttpStatus status) {
    this(status, null, null);
  }

  public WebException(HttpStatus status, String message) {
    this(status, message, null);
  }

  public WebException(HttpStatus status, String message, Throwable t) {
    super(message, t);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }
}
