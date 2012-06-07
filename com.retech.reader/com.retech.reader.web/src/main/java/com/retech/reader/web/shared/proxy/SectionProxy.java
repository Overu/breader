package com.retech.reader.web.shared.proxy;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.core.shared.rpc.BaseEntityProxy;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import com.retech.reader.web.shared.Section;


import java.util.List;

@ProxyFor(value = Section.class, locator = RequestFactoryLocator.class)
public interface SectionProxy extends BaseEntityProxy {

  String WITH = "issue";

  IssueProxy getIssue();

  int getPageCount();

  @Deprecated
  List<PageProxy> getPages();

  int getSequence();

  String getTitle();

  @Override
  EntityProxyId<SectionProxy> stableId();
}
