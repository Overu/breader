package org.cloudlet.web.security.server.domain;

import org.cloudlet.web.service.server.jpa.BaseDomain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Role extends BaseDomain {
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
