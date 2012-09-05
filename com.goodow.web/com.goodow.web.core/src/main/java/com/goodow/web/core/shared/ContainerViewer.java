package com.goodow.web.core.shared;

public enum ContainerViewer implements ViewType {

  ENTRY, FEED;

  @Override
  public boolean acceptsFeed() {
    return false;
  }

  @Override
  public String getName() {
    return name().toLowerCase();
  }

  @Override
  public String getTitle() {
    return name();
  }
}