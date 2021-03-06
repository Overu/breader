package com.goodow.web.mvp.shared.tree;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.mvp.shared.TreeNode;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;


import java.util.List;

@ProxyFor(value = TreeNode.class, locator = RequestFactoryLocator.class)
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