package org.cloudlet.web.security.server.auth;

import com.goodow.wave.test.BaseTest;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.cloudlet.web.security.server.domain.Permission;
import org.cloudlet.web.security.server.domain.Role;
import org.cloudlet.web.security.server.domain.User;
import org.cloudlet.web.security.server.service.UserService;
import org.cloudlet.web.service.server.jpa.BaseService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

public class JpaRealmTest extends BaseTest {
  public static class JpaRealmTestModule extends BaseTestModule {
    @Override
    protected void configure() {
      super.configure();
      bind(new TypeLiteral<BaseService<Role>>() {
      }).toInstance(new BaseService<Role>() {
      });
      bind(new TypeLiteral<BaseService<Permission>>() {
      }).toInstance(new BaseService<Permission>() {
      });
    }
  }

  String userName = "admin";
  String pwd = "1234";
  @Inject
  private Provider<User> users;
  @Inject
  private UserService userService;
  @Inject
  private Provider<Role> roles;
  @Inject
  private Provider<Permission> permissions;
  @Inject
  SecurityManager securityManager;
  @Inject
  private BaseService<Role> roleService;
  @Inject
  private BaseService<Permission> permissionService;
  @Inject
  private Provider<EntityManager> em;

  @Before
  public void initShiro() {
    SecurityUtils.setSecurityManager(securityManager);
  }

  @Before
  public void resetData() {
    em.get().getTransaction().begin();
    User user = userService.findUserByUsername(userName);
    if (user != null) {
      List<Role> roles = user.getRoles();
      if (roles != null) {
        for (Role role : roles) {
          List<Permission> permissions = role.getPermissions();
          if (permissions != null) {
            for (Permission perm : permissions) {
              permissionService.remove(perm);
            }
          }
          roleService.remove(role);
        }
      }
      userService.remove(user);
    }

    List<Permission> perms = new ArrayList<Permission>();
    Permission perm0 = permissions.get().setPermissionName("permission 0");
    permissionService.put(perm0);
    perms.add(perm0);
    Permission perm1 = permissions.get().setPermissionName("permission 1");
    permissionService.put(perm1);
    perms.add(perm1);

    Role role = roles.get().setRoleName("role 0");
    roleService.put(role);
    role.setPermissions(perms);

    String hashedPwd =
        new SimpleHash(JpaRealm.ALGORITHM_NAME, pwd.toCharArray(), ByteSource.Util.bytes(pwd))
            .toHex();
    user = users.get().setUserName(userName).setPassword(hashedPwd).setPasswordSalt(pwd);
    // 不能使用 Arrays.asList(role)
    List<Role> r = new ArrayList<Role>();
    r.add(role);
    user.setRoles(r);

    userService.put(user);
    em.get().getTransaction().commit();
  }

  @Test
  public void testLogin() {
    UsernamePasswordToken token = new UsernamePasswordToken(userName, pwd);
    Subject currentUser = SecurityUtils.getSubject();
    try {
      currentUser.login(token);
      userService.updatePassword(userName, pwd);
      // } catch (UnknownAccountException uae) {
      // } catch (IncorrectCredentialsException ice) {
      // } catch (LockedAccountException lae) {
      // } catch (ExcessiveAttemptsException eae) {
      // // } ... catch your own ...
      // } catch (AuthenticationException ae) {
      // // unexpected error?
    } finally {
      currentUser.logout();
    }

    // No problems, continue on as expected...
  }

  @Test
  public void testPermissonViolate() {
    try {
      userService.updatePassword(userName, pwd);
    } catch (UnauthenticatedException e) {
      return;
    }
    fail("should not reach here");
  }

  @Override
  protected Class<? extends Module> providesModule() {
    return JpaRealmTestModule.class;
  }

}
