package com.goodow.web.mvp.shared.tree;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

import java.util.List;

public final class TreeCategory {
  public static boolean equals(AutoBean<? extends TreeNodeProxy> bean, Object o) {
    if (!(o instanceof TreeNodeProxy)) {
      return false;
    }
    AutoBean<TreeNodeProxy> other = AutoBeanUtils.getAutoBean((TreeNodeProxy) o);
    if (other == null) {
      // Unexpected, could be an user-provided implementation?
      return false;
    }

    return other.as().getPath().equals(bean.as().getPath());
  }

  public static <T extends TreeNodeProxy> String toString(AutoBean<? extends T> bean) {
    T as = bean.as();
    return toString(as);
  }

  private static final String toString(TreeNodeProxy root) {
    StringBuilder sb = new StringBuilder();
    toString(root, sb, "");
    return sb.toString();
  }

  private static final void toString(TreeNodeProxy node, StringBuilder sb, String indent) {
    if (sb.length() > 0) {
      sb.append('\n');
    }
    sb.append(indent);
    sb.append(' ');
    sb.append(node.getPath());
    sb.append(":");
    sb.append(node.getName());
    sb.append(":");
    sb.append(node.getType());
    List<TreeNodeProxy> children = node.getChildren();
    if (children == null) {
      return;
    }
    for (TreeNodeProxy child : children) {
      toString(child, sb, indent + "  ");
    }
  }

}
