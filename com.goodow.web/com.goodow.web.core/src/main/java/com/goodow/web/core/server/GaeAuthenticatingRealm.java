package com.goodow.web.core.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * A {@link Realm} which uses {@link UserService} to authenicate users
 */
public class GaeAuthenticatingRealm extends AuthorizingRealm {
  public GaeAuthenticatingRealm() {
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user == null) {
      throw new AccountException("Not authenticated.");
    }

    return new SimpleAuthenticationInfo(user, null, getName());
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
    // We perform Authentication only, thus return null
    return null;
  }

  @Override
  protected void onInit() {
    // We use UserService to authenticate users,
    // thus dont have any credentials
    setCredentialsMatcher(new AllowAllCredentialsMatcher());
  }
}