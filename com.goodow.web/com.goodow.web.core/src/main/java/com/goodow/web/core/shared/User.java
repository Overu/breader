package com.goodow.web.core.shared;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_user")
public class User extends Content {

  @NotNull(message = "你必须指定用户名")
  private String userName;
  private String password;

  private String passwordSalt;

  public String getPassword() {
    return password;
  }

  public String getPasswordSalt() {
    return passwordSalt;
  }

  public String getUserName() {
    return userName;
  }

  public User setPassword(final String password) {
    this.password = password;
    return this;
  }

  public User setPasswordSalt(final String passwordSalt) {
    this.passwordSalt = passwordSalt;
    return this;
  }

  public User setUserName(final String userName) {
    this.userName = userName;
    return this;
  }

}
