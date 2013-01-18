package com.goodow.web.core.shared;

public enum FeedViewer implements ViewType {

  NEW, ALL_CONTENT, MY_CONTENT, SELECTED_CONTENT, RECOMMEND, FAVORITES, DISCOUNTED, MOSTVIEWED, CATEGORIZED, BOOKSEARCH;

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
    case RECOMMEND:
      return "精品推荐";
    case FAVORITES:
      return "我的收藏";
    case DISCOUNTED:
      return "特价促销";
    case MOSTVIEWED:
      return "热门图书";
    case CATEGORIZED:
      return "分类浏览";
    case BOOKSEARCH:
      return "搜索";
    default:
      break;
    }
    return name();
  }
}