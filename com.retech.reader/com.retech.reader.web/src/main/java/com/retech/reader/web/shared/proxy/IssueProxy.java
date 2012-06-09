package com.retech.reader.web.shared.proxy;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.mvp.shared.tree.rpc.BaseEntityProxy;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import com.retech.reader.web.shared.Issue;

import java.util.Date;
import java.util.List;

@ProxyFor(value = Issue.class, locator = RequestFactoryLocator.class)
public interface IssueProxy extends BaseEntityProxy {

  String ISSUES = "issue.all";
  String MY_ISSUES = "issue.my";
  String COUNT_ISSUE = "issue.count";
  String ISSUE_DOWN_NAME = "我的书架";
  String MY_ISSUES_NAME = "我的收藏";
  String ISSUE_DOWN = "issue.down";
  String ISSUE_DOWN_FINISH = "issue.down.finish";

  String ISSUE_STATE_COLLECT = "收藏";
  String ISSUE_STATE_COLLECTED = "已收藏";
  String ISSUE_STATE_DOWN = "下载";
  String ISSUE_STATE_DOWN_FINISH = "已下载";
  String ISSUE_STATE_ONLINE_READ = "在线阅读";
  String ISSUE_STATE_DOWN_READ = "阅读";

  String HELP_ISSUE = "help.issue";

  CategoryProxy getCategory();

  Date getCreateTime();

  String getDetail();

  @Deprecated
  ResourceProxy getImage();

  @Deprecated
  List<SectionProxy> getSections();

  String getTitle();

  Date getUpdateTime();

  Long getVersion();

  long getViewCount();

  IssueProxy setCreateTime(Date createTime);

  IssueProxy setDetail(String detail);

  IssueProxy setTitle(String title);

  IssueProxy setUpdateTime(Date updateTime);

  IssueProxy setViewCount(long viewCount);

  @Override
  EntityProxyId<IssueProxy> stableId();

}
