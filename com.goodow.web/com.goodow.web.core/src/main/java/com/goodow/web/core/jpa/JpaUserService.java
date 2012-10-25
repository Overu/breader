package com.goodow.web.core.jpa;

import com.goodow.web.core.shared.User;
import com.goodow.web.core.shared.UserService;

import com.google.inject.persist.Transactional;

import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import javax.persistence.NoResultException;

@Transactional
public class JpaUserService extends JpaEntityService<User> implements UserService {

  @Override
  public User findUserByUsername(final String userName) {
    User toRtn = null;
    try {
      toRtn =
          em.get()
              .createQuery(
                  "select u from com.goodow.web.security.server.domain.User u where u.userName = :userName",
                  User.class).setParameter("userName", userName).getSingleResult();
    } catch (NoResultException e) {
    }
    return toRtn;
  }

  @Override
  @RequiresAuthentication
  public void updatePassword(final String userName, final String newPwd) {
    User user = findUserByUsername(userName);
    if (user == null) {
      throw new UnknownAccountException("找不到用户名: " + userName);
    }
    String hashedPwd =
        new SimpleHash(JpaRealm.ALGORITHM_NAME, newPwd.toCharArray(), ByteSource.Util.bytes(user
            .getPhone())).toHex();
    user.setEmail(hashedPwd);
    em.get().persist(user);
  }
}
