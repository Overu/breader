package com.goodow.web.security.server.domain;

import com.goodow.web.service.server.jpa.BaseDomain;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Permission extends BaseDomain {
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
