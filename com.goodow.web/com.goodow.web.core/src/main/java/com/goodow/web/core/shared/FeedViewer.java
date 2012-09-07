package com.goodow.web.core.shared;

public enum FeedViewer implements ViewType {

  NEW, ALL_CONTENT, MY_CONTENT, SELECTED_CONTENT;

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
      case NEW:
        return "制作新书";
      case ALL_CONTENT:
        return "所有图书";
      case MY_CONTENT:
        return "我的图书";
      case SELECTED_CONTENT:
        return "精选图书";
      default:
        break;
    }
    return name();
  }
}