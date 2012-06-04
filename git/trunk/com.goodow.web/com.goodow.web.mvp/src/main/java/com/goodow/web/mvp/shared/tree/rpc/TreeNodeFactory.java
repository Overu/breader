package com.goodow.web.mvp.shared.tree.rpc;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface TreeNodeFactory extends RequestFactory {
  TreeNodeContext treeNodeContext();
}
