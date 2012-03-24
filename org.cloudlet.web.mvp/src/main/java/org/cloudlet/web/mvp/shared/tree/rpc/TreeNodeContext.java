package org.cloudlet.web.mvp.shared.tree.rpc;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import org.cloudlet.web.mvp.server.tree.service.TreeService;
import org.cloudlet.web.mvp.shared.tree.TreeNodeProxy;
import org.cloudlet.web.service.server.NoLocator;

import java.util.List;

@Service(value = TreeService.class, locator = NoLocator.class)
public interface TreeNodeContext extends RequestContext {
  Request<List<TreeNodeProxy>> find(int start, int length, String sort);

  Request<Void> put(TreeNodeProxy proxy);
}
