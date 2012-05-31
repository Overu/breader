package com.goodow.web.mvp.shared.tree.rpc;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.mvp.server.tree.service.TreeService;
import com.goodow.web.mvp.shared.tree.TreeNodeProxy;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;


import java.util.List;

@Service(value = TreeService.class, locator = RequestFactoryLocator.class)
public interface TreeNodeContext extends RequestContext {
  Request<List<TreeNodeProxy>> find(int start, int length, String sort);

  Request<Void> put(TreeNodeProxy proxy);
}
