package com.goodow.web.mvp.shared.tree;

import com.goodow.web.mvp.shared.BasePlace;
import com.goodow.web.mvp.shared.Default;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.RequestFactory;


public final class TreeNodePlace extends BasePlace {

  public static final String TOKEN_PREFIX = "!";
  private final TreeNodeUtil util;
  private final TreeNodeProxy root;

  @Inject
  TreeNodePlace(final TreeNodeUtil util, @Default final TreeNodeProxy root,
      final PlaceHistoryMapper placeHistoryMapper, final RequestFactory f) {
    super(placeHistoryMapper, f);
    this.util = util;
    this.root = root;
  }

  @Override
  public String getPath() {
    String path = super.getPath();
    if (path == null || path.isEmpty()) {
      return "";
    }
    path = findFirstLeaf(path);
    boolean start = path.startsWith(BasePlace.PATH_SEPARATOR);
    boolean end =
        path.length() > BasePlace.PATH_SEPARATOR.length()
            && path.endsWith(BasePlace.PATH_SEPARATOR);
    path =
        path.substring(start ? BasePlace.PATH_SEPARATOR.length() : 0, end ? path.length()
            - BasePlace.PATH_SEPARATOR.length() : path.length());
    return path;
  }

  public TreeNodeProxy getTopNode() {
    String prefix = "";
    for (String part : getPath().split(BasePlace.PATH_SEPARATOR)) {
      if ("".equals(part)) {
        continue;
      }
      TreeNodeProxy childNode = util.findChild(root, prefix + part);
      if (childNode != null) {
        return childNode;
      } else {
        prefix += (part + BasePlace.PATH_SEPARATOR);
      }
    }
    return null;

  }

  public TreeNodeProxy getTreeNode() {
    TreeNodeProxy node = util.find(root, this.getPath());
    return node;
  }

  @Override
  public TreeNodePlace setPath(final String path) {
    super.setPath(path);
    return this;
  }

  /**
   * 取得带有内容的第一个子节点
   * 
   * @return
   */
  private String findFirstLeaf(final String path) {
    TreeNodeProxy node = util.find(root, path);
    if (node == null || node.getType() != null || node.getChildren() == null
        || node.getChildren().isEmpty()) {
      return path;
    }
    TreeNodeProxy firstChild = node.getChildren().get(0);
    return findFirstLeaf(firstChild.getPath());
  }

}
