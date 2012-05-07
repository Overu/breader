package com.retech.reader.web.shared.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import com.retech.reader.web.server.domain.Issue;

import org.cloudlet.web.service.server.NoLocator;
import org.cloudlet.web.service.shared.rpc.BaseEntityProxy;

import java.util.Date;
import java.util.List;

@ProxyFor(value = Issue.class, locator = NoLocator.class)
public interface IssueProxy extends BaseEntityProxy {

  String ISSUES = "issue.all";
  String MY_ISSUES = "issue.my";
  String COUNT_ISSUE = "issue.count";
  String ISSUE_DOWN = "issue.down";

  CategoryProxy getCategory();

  Date getCreateTime();

  String getDetail();

  @Deprecated
  Long getId();

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
