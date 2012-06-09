package com.retech.reader.web.shared.proxy;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.mvp.shared.tree.rpc.BaseEntityProxy;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import com.retech.reader.web.shared.Page;


import java.util.List;

@ProxyFor(value = Page.class, locator = RequestFactoryLocator.class)
public interface PageProxy extends BaseEntityProxy {

  String WITH = "section.issue";

  String getMainResourceFilename();

  int getPageNum();

  @Deprecated
  List<ResourceProxy> getResources();

  SectionProxy getSection();

  String getTitle();

  @Override
  EntityProxyId<PageProxy> stableId();

}
