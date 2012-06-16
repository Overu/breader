package com.goodow.web.security.server.auth;

import com.goodow.wave.test.BaseTest;
import com.goodow.web.core.jpa.JpaRealm;
import com.goodow.web.core.shared.User;
import com.goodow.web.core.shared.UserService;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

public class DataImportTest extends BaseTest {

  public static List<String> getName() throws IOException {
    InputStream inputStream = DataImportTest.class.getResourceAsStream("三国人物.txt");
    BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
    List<String> list = new ArrayList<String>();
    String str = null;
    while ((str = bf.readLine()) != null) {
      list.add(str);
    }
    inputStream.close();
    bf.close();
    return list;
  }

  String userName = null;
  String pwd = "123";
  @Inject
  private Provider<User> users;
  @Inject
  private UserService userService;
  @Inject
  SecurityManager securityManager;

  @Inject
  private Provider<EntityManager> em;

  @Test
  public void testData() throws IOException {
    em.get().getTransaction().begin();
    List<String> list = DataImportTest.getName();
    User user = null;
    for (String pm : list) {
      userName = pm;
      user = userService.findUserByUsername(userName);
      if (user != null) {
        userService.remove(user);
      }
      String hashedPwd =
          new SimpleHash(JpaRealm.ALGORITHM_NAME, pwd.toCharArray(), ByteSource.Util.bytes(pwd))
              .toHex();
      user = users.get().setUserName(userName).setPassword(hashedPwd).setPasswordSalt(pwd);
      userService.put(user);
    }

    em.get().getTransaction().commit();
  }
}
