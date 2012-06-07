package com.goodow.web.security.shared;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_role")
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
