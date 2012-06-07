package com.goodow.web.security.server;

import com.goodow.web.security.shared.User;
import com.goodow.web.security.shared.UserService;

public class UserServiceImpl extends ContentServiceImpl<User> implements UserService {

  public User save(final User user) {
    em.get().persist(user);
    return user;
  }

}
