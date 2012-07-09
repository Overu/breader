package com.goodow.web.core.shared;

import java.io.Serializable;
import java.util.List;

public class Response<T> implements Serializable {

  private FailureMessage generalFailure;

  private boolean success;

  private T result;

  private List<Violation> violations;

  private Message message;

  public Response(final Message message) {
    this.message = message;
  }

  public FailureMessage getGeneralFailure() {
    return generalFailure;
  }

  public T getResult() {
    return result;
  }

  public List<Violation> getViolations() {
    return violations;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setGeneralFailure(final FailureMessage failure) {
    this.generalFailure = failure;
  }

  public void setResult(final T result) {
    this.result = result;
  }

  public void setSuccess(final boolean success) {
    this.success = success;
  }

  public void setViolations(final List<Violation> value) {
    this.violations = value;
  }
}
