package org.cloudlet.web.security.server.service;

import com.google.inject.persist.Transactional;

import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.cloudlet.web.security.server.auth.JpaRealm;
import org.cloudlet.web.security.server.domain.User;
import org.cloudlet.web.service.server.jpa.BaseService;

import javax.persistence.NoResultException;

@Transactional
public class UserService extends BaseService<User> {
  public User findUserByUsername(final String userName) {
    User toRtn = null;
    try {
      toRtn =
          em.get()
              .createQuery(
                  "select u from org.cloudlet.web.security.server.domain.User u where u.userName = :userName",
                  User.class).setParameter("userName", userName).getSingleResult();
    } catch (NoResultException e) {
    }
    return toRtn;
  }

  @RequiresAuthentication
  public void updatePassword(final String userName, final String newPwd) {
    User user = findUserByUsername(userName);
    if (user == null) {
      throw new UnknownAccountException("找不到用户名: " + userName);
    }
    String hashedPwd =
        new SimpleHash(JpaRealm.ALGORITHM_NAME, newPwd.toCharArray(), ByteSource.Util.bytes(user
            .getPasswordSalt())).toHex();
    user.setPassword(hashedPwd);
    put(user);
  }
}
