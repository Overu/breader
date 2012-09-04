package com.goodow.web.core.shared;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@TypeDefs({
    @TypeDef(name = "role", typeClass = RoleType.class),
    @TypeDef(name = "principal", typeClass = PrincipalType.class)})
@SuppressWarnings("serial")
@Entity
@Table(name = "t_member")
public class Member extends WebContent {

  @Type(type = "principal")
  @Columns(columns = {@Column(name = "principalType"), @Column(name = "principalName")})
  private WebContent principal;

  @Type(type = "role")
  @Columns(columns = {@Column(name = "roleType"), @Column(name = "roleName")})
  private Role role;

  @Column
  private String comment;

  public String getComment() {
    return comment;
  }

  public WebContent getPrincipal() {
    return principal;
  }

  public Role getRole() {
    return role;
  }

  public void setComment(final String comment) {
    this.comment = comment;
  }

  public void setPrincipal(final WebContent principal) {
    this.principal = principal;
  }

  public void setRole(final Role role) {
    this.role = role;
  }

}
