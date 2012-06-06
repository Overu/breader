package com.goodow.web.security.server.domain;

import com.goodow.web.security.shared.Content;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Role extends Content {
  @NotNull(message = "你必须指定角色名称")
  private String roleName;
  @OneToMany
  private List<Permission> permissions;

  public List<Permission> getPermissions() {
    return permissions;
  }

  public String getRoleName() {
    return roleName;
  }

  public Role setPermissions(final List<Permission> permissions) {
    this.permissions = permissions;
    return this;
  }

  public Role setRoleName(final String roleName) {
    this.roleName = roleName;
    return this;
  }
}
