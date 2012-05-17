package com.retech.reader.web.shared.rpc;

import com.goodow.wave.server.rf.RfLocator;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;

import com.retech.reader.web.server.service.ResourceService;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;

import org.cloudlet.web.service.shared.rpc.BaseContext;

@Service(value = ResourceService.class, locator = RfLocator.class)
public interface ResourceContext extends BaseContext {

  @Deprecated
  Request<String> getDataString(final Long id);

  Request<ResourceProxy> getImage(IssueProxy issue);

  Request<ResourceProxy> getResource(PageProxy page);

}
