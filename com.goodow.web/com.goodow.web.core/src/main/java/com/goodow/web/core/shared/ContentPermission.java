package com.goodow.web.core.shared;

public enum ContentPermission implements com.goodow.web.core.shared.Permission {

  // 查看
  READ,

  // 添加
  ADD,

  // 修改我添加的内容
  EDIT_OWN,

  // 删除我添加的记录
  DELETE_OWN,

  // 修改任何内容
  EDIT,

  // 删除任何内容
  DELETE,

  // 批准、拒绝、隔离内容
  MODERATE,

  // 移动、永久刪除等其他管理员操作
  ADMIN;
}
