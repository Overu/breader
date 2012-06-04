package com.goodow.web.security.server.auth;

import com.goodow.web.security.server.domain.Permission;
import com.goodow.web.security.server.domain.Role;
import com.goodow.web.security.server.domain.User;
import com.goodow.web.security.server.service.UserService;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.EntityManager;

public class JpaRealm extends AuthorizingRealm {

  public static final String ALGORITHM_NAME = Sha1Hash.ALGORITHM_NAME;
  private final Provider<EntityManager> em;
  private final UserService userService;

  @Inject
  JpaRealm(final Provider<EntityManager> em, final UserService userService) {
    this.em = em;
    this.userService = userService;

    HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(ALGORITHM_NAME);
    setCredentialsMatcher(matcher);
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token)
      throws AuthenticationException {
    UsernamePasswordToken upToken = (UsernamePasswordToken) token;
    String userName = upToken.getUsername();

    // Null username is invalid
    if (userName == null) {
      throw new AccountException("Null usernames are not allowed by this realm.");
    }

    User user = userService.findUserByUsername(userName);
    if (user == null) {
      return null;
    }
    SimpleAuthenticationInfo info = null;
    info = new SimpleAuthenticationInfo(userName, user.getPassword().toCharArray(), getName());

    if (user.getPasswordSalt() != null) {
      info.setCredentialsSalt(ByteSource.Util.bytes(user.getPasswordSalt()));
    }
    return info;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
    // null usernames are invalid
    if (principals == null) {
      throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
    }

    String userName = (String) getAvailablePrincipal(principals);
    User user = userService.findUserByUsername(userName);
    if (user.getRoles() == null) {
      throw new AuthorizationException("请先为该用户配置角色信息");
    }
    Set<String> roleNames = new LinkedHashSet<String>();
    Set<String> permissions = new LinkedHashSet<String>();
    for (Role role : user.getRoles()) {
      roleNames.add(role.getRoleName());
      if (role.getPermissions() != null) {
        for (Permission permission : role.getPermissions()) {
          permissions.add(permission.getPermissionName());
        }
      }
    }

    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
    info.setStringPermissions(permissions);
    return info;
  }

}
