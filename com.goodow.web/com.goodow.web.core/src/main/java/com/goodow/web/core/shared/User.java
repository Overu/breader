package com.goodow.web.core.shared;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_user")
public class User extends WebEntity implements Principal {

  @NotNull(message = "你必须指定用户名")
  private String loginId;

  private String password;

  private String passwordSalt;

  public String getLoginId() {
    return loginId;
  }

  public String getPassword() {
    return password;
  }

  public String getPasswordSalt() {
    return passwordSalt;
  }

  public void setLoginId(final String userName) {
    this.loginId = userName;
  }

  public User setPassword(final String password) {
    this.password = password;
    return this;
  }

  public User setPasswordSalt(final String passwordSalt) {
    this.passwordSalt = passwordSalt;
    return this;
  }

}
