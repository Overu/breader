package com.goodow.web.mvp.shared.tree;

import com.goodow.web.mvp.shared.BasePlace;

import com.google.inject.Inject;
import com.google.inject.Singleton;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public final class TreeNodeUtil {

  private static Logger logger = Logger.getLogger(TreeNodeUtil.class.getName());

  @Inject
  TreeNodeUtil() {
  }

  public final TreeNodeProxy add(TreeNodeProxy root, TreeNodeProxy child) {

    TreeNodeProxy parent = findMostSpecificNode(root, child.getPath(), null);
    if (parent.getChildren() == null) {
      parent.setChildren(new ArrayList<TreeNodeProxy>());
    }
    parent.getChildren().add(child);
    return parent;
  }

  public final TreeNodeProxy find(TreeNodeProxy root, String path) {
    if (root == null) {
      return null;
    }
    TreeNodeProxy mostSpecificNode = findMostSpecificNode(root, path, null);
    if (mostSpecificNode.getPath().contains(path)) {
      return mostSpecificNode;
    } else {
      return null;
    }
  }

  // public static final native MenuNodeJson parseMenuNodeFromJson(String jsonString) /*-{
  // return eval('('+jsonString+')');
  // }-*/;

  // public static final native UserInfoJson parseUserInfoFromJson(String jsonString) /*-{
  // return eval('(' + jsonString + ')');
  // }-*/;

  public final TreeNodeProxy findChild(TreeNodeProxy node, String part) {
    if (node == null) {
      return null;
    }
    String id = node.getPath() + part + BasePlace.PATH_SEPARATOR;
    List<? extends TreeNodeProxy> children = node.getChildren();
    if (children == null) {
      return null;
    }
    for (int k = 0, n = children.size(); k < n; k++) {
      if (children.get(k).getPath().equals(id)) {
        return children.get(k);
      }
    }
    return null;
  }

  public final TreeNodeProxy findMostSpecificNode(TreeNodeProxy root, String path, String type) {
    // String rootString = MenuNodeUtil.toString(root);
    TreeNodeProxy mostSpecificNode = root;
    TreeNodeProxy mostSpecificNodeByType =
        (type == null || root.getType().equals(type)) ? root : null;
    String prefix = "";
    for (String part : path.split(BasePlace.PATH_SEPARATOR)) {
      if ("".equals(part)) {
        continue;
      }
      TreeNodeProxy childNode = findChild(mostSpecificNode, prefix + part);
      if (childNode != null) {
        // Follow existing branch.
        mostSpecificNode = childNode;
        prefix = "";
        if (type == null || childNode.getType().equals(type)) {
          // String toString = MenuNodeUtil.toString(childNode);
          mostSpecificNodeByType = childNode;
        }
      } else {
        prefix += (part + BasePlace.PATH_SEPARATOR);
      }
    }
    return mostSpecificNodeByType;
  }

  private List<TreeNodeProxy> getChildrenByType(TreeNodeProxy node, String nodeType) {
    List<TreeNodeProxy> toReturn = new ArrayList<TreeNodeProxy>();
    for (TreeNodeProxy childNode : node.getChildren()) {
      if (childNode.getType().equals(nodeType)) {
        toReturn.add(childNode);
      }
    }
    return toReturn;
  }
}