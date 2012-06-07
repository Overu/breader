package com.goodow.web.security.shared;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlType;

@Entity
@XmlType
@Table(name = "t_permission")
public class Permission extends Content {

  @NotNull(message = "你必须指定权限名称")
  private String permissionName;

  public String getPermissionName() {
    return permissionName;
  }

  public Permission setPermissionName(final String permissionName) {
    this.permissionName = permissionName;
    return this;
  }
}
