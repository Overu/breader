package com.retech.reader.web.shared.rpc;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.mvp.shared.tree.rpc.BaseContext;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;

import com.retech.reader.web.jpa.JpaResourceService;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;


@Service(value = JpaResourceService.class, locator = RequestFactoryLocator.class)
public interface ResourceContext extends BaseContext {

  @Deprecated
  Request<String> getDataString(final Long id);

  Request<ResourceProxy> getImage(IssueProxy issue);

  Request<ResourceProxy> getResource(PageProxy page);

}
