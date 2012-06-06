package com.goodow.web.mvp.server.tree.domain;

import com.goodow.web.core.server.InjectionListener;
import com.goodow.web.security.shared.Content;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;

@Entity
@EntityListeners(InjectionListener.class)
public class TreeNode extends Content {

  private String path;
  private String name;
  private String type;
  @OneToMany(cascade = CascadeType.ALL)
  private List<TreeNode> children;

  @Inject
  private transient Provider<EntityManager> em;

  public List<TreeNode> getChildren() {
    return children;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public String getType() {
    return type;
  }

  public void put() {
    em.get().persist(this);
  }

  public void setChildren(final List<TreeNode> children) {
    this.children = children;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setPath(final String path) {
    this.path = path;
  }

  public void setType(final String type) {
    this.type = type;
  }

}
