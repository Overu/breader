package com.goodow.web.core.shared;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum CoreRole implements Role {

  // 查看、申请加入团队空间
  GUEST(GroupPermission.READ, ContentPermission.READ, MemberPermission.REQUEST),

  // 添加、修改、删除自己的内容，以及创建团队子空间
  CONTRIBUTOR(GUEST, GroupPermission.ADD_CHILD, ContentPermission.ADD, ContentPermission.EDIT_OWN,
      ContentPermission.DELETE_OWN),

  // 修改、刪除、审批团队空间内容
  MODERATOR(CONTRIBUTOR, ContentPermission.EDIT, ContentPermission.DELETE,
      ContentPermission.MODERATE),

  // 修改、删除空间，管理应用组件、团队成员，移动和永久删除空间内容等
  MANAGER(MODERATOR, GroupPermission.ADMIN, MemberPermission.ADMIN, ContentPermission.ADMIN);

  private final Set<Permission> permissions;

  private Set<Role> implicitRoles;

  public static List<CoreRole> listValues = Arrays.asList(values());

  private CoreRole(final Permission... permissions) {
    this((Role[]) null, permissions);
  }

  private CoreRole(final Role role, final Permission... permissions) {
    this(new Role[] {role}, permissions);
  }

  private CoreRole(final Role[] roles, final Permission... permissions) {
    Set<Permission> temp = new HashSet<Permission>();
    if (roles != null) {
      for (Role role : roles) {
        temp.addAll(role.getPermissions());
      }
    }
    for (Permission perm : permissions) {
      temp.add(perm);
    }
    this.permissions = Collections.unmodifiableSet(temp);
  }

  @Override
  public String getDescription() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<Role> getImplicitRoles() {
    if (implicitRoles == null) {
      implicitRoles = new HashSet<Role>();
      for (Role role : CoreRole.values()) {
        if (this.getPermissions().containsAll(role.getPermissions())) {
          implicitRoles.add(role);
        }
      }
      implicitRoles = Collections.unmodifiableSet(implicitRoles);
    }
    return implicitRoles;
  }

  @Override
  public String getLocalizedName() {
    switch (this) {
      case MANAGER:
        return GroupRoleNames.instance.manager();
      case MODERATOR:
        return GroupRoleNames.instance.moderator();
      case CONTRIBUTOR:
        return GroupRoleNames.instance.contributor();
      case GUEST:
        return GroupRoleNames.instance.guest();
    }
    return null;
  }

  @Override
  public Set<Permission> getPermissions() {
    return permissions;
  }
}