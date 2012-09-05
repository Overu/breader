package com.goodow.web.core.shared;

public enum FeedViewer implements ViewType {

  FORM, ALL_CONTENT, MY_CONTENT, SELECTED_CONTENTE;

  @Override
  public boolean acceptsFeed() {
    return true;
  }

  @Override
  public String getName() {
    return name().toLowerCase();
  }

  @Override
  public String getTitle() {
    switch (this) {
      case FORM:
        return "制作新书";
      case ALL_CONTENT:
        return "所有图书";
      case MY_CONTENT:
        return "我的图书";
      case SELECTED_CONTENTE:
        return "精选图书";
      default:
        break;
    }
    return name();
  }
}