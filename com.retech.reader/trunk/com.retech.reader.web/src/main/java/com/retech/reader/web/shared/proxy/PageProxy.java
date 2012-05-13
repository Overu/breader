package com.retech.reader.web.shared.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import com.retech.reader.web.server.domain.Page;

import org.cloudlet.web.service.server.NoLocator;
import org.cloudlet.web.service.shared.rpc.BaseEntityProxy;

import java.util.List;

@ProxyFor(value = Page.class, locator = NoLocator.class)
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
