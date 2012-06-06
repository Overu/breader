package com.goodow.web.core.shared;

public enum HttpStatus {

  OK(200), CREATED(201), BAD_REQUEST(400), UNAUTHORIZED(401), FORBIDDEN(403), NOT_FOUND(404), FATAL(
      500);

  private int code;

  private HttpStatus(int code) {
    this.code = code;
  }

}
