package com.goodow.web.core.shared;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Request<T> implements Serializable {

  private transient Message message;

  private transient Operation operation;

  private Request<?> nextRequest;

  private transient Object[] args;

  private transient Receiver<T> receiver;

  private String contentType;

  public Request(final Message message) {
    this.message = message;
  }

  public Response fire() {
    return message.fire().getResponse();
  }

  public Response fire(final Receiver<T> receiver) {
    return to(receiver).fire();
  }

  public Object[] getArgs() {
    return args;
  }

  public String getContentType() {
    return contentType;
  }

  public Message getMessage() {
    return message;
  }

  public Request<?> getNextRequest() {
    return nextRequest;
  }

  public Operation getOperation() {
    return operation;
  }

  public Receiver<T> getReceiver() {
    return receiver;
  }

  public void setArgs(final Object[] args) {
    this.args = args;
  }

  public Request<T> setContentType(final String contentType) {
    this.contentType = contentType;
    return this;
  }

  public void setNextRequest(final Request<?> nextRequest) {
    this.nextRequest = nextRequest;
  }

  public void setOperation(final Operation operation) {
    this.operation = operation;
  }

  public Request<T> to(final Receiver<T> receiver) {
    this.receiver = receiver;
    return this;
  }

}
