package com.goodow.web.core.client;

import com.goodow.web.core.shared.*;
public class ClientUserService extends ClientService<User> {
  public Request<User> findUserByUsername(java.lang.String userName) {
    return invoke(CorePackage.UserService_findUserByUsername, userName);
  }
  
  public Request<Void> updatePassword(java.lang.String userName, java.lang.String newPwd) {
    return invoke(CorePackage.UserService_updatePassword, userName, newPwd);
  }
}
