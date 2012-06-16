package com.goodow.web.core.shared;

public enum GroupPermission implements com.goodow.web.core.shared.Permission {

  READ,

  // add child group
  ADD_CHILD,

  // 修改、删除空间
  ADMIN;
}
