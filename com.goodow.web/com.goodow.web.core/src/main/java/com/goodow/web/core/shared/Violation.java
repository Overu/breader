package com.goodow.web.core.shared;

import java.io.Serializable;

public class Violation implements Serializable {

  private EntityId leafBeanId;

  private String message;

  private String messageTemplate;

  private String path;

  private EntityId rootBeanId;

  public EntityId getLeafBeanId() {
    return leafBeanId;
  }

  public String getMessage() {
    return message;
  }

  public String getMessageTemplate() {
    return messageTemplate;
  }

  public String getPath() {
    return path;
  }

  public EntityId getRootBeanId() {
    return rootBeanId;
  }

  public void setLeafBeanId(final EntityId id) {
    this.leafBeanId = id;
  }

  public void setMessage(final String value) {
    this.message = value;
  }

  public void setMessageTemplate(final String value) {
    this.messageTemplate = value;
  }

  public void setPath(final String value) {
    this.path = value;
  }

  public void setRootBeanId(final EntityId id) {
    this.rootBeanId = id;
  }
}
