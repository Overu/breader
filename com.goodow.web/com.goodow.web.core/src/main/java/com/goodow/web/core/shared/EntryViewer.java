package com.goodow.web.core.shared;

public enum EntryViewer implements ViewType {

  EDIT, BOOKDETAIL;

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