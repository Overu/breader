package org.cloudlet.web.mvp.shared.tree;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import org.cloudlet.web.mvp.server.tree.domain.TreeNode;
import org.cloudlet.web.service.server.NoLocator;

import java.util.List;

@ProxyFor(value = TreeNode.class, locator = NoLocator.class)
public interface TreeNodeProxy extends EntityProxy {

  List<TreeNodeProxy> getChildren();

  String getName();

  String getPath();

  String getType();

  TreeNodeProxy setChildren(List<TreeNodeProxy> children);

  TreeNodeProxy setName(String name);

  TreeNodeProxy setPath(String path);

  TreeNodeProxy setType(String type);

  @Override
  EntityProxyId<TreeNodeProxy> stableId();
}