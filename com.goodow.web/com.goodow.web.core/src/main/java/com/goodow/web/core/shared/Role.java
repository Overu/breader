package com.goodow.web.core.shared;

import java.io.Serializable;
import java.util.Set;

public interface Role extends Serializable {

  String getDescription();

  Set<Role> getImplicitRoles();

  String getLocalizedName();

  Set<Permission> getPermissions();

  String name();

  int ordinal();
}
