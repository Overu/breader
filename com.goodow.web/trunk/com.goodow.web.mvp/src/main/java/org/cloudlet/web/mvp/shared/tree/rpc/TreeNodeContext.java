package org.cloudlet.web.mvp.shared.tree.rpc;

import com.goodow.wave.server.rf.RfLocator;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import org.cloudlet.web.mvp.server.tree.service.TreeService;
import org.cloudlet.web.mvp.shared.tree.TreeNodeProxy;

import java.util.List;

@Service(value = TreeService.class, locator = RfLocator.class)
public interface TreeNodeContext extends RequestContext {
  Request<List<TreeNodeProxy>> find(int start, int length, String sort);

  Request<Void> put(TreeNodeProxy proxy);
}
