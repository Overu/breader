package com.goodow.web.core.shared;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@TypeDef(name = "role", typeClass = RoleType.class)
@SuppressWarnings("serial")
@Entity
@Table(name = "t_member")
public class Member extends Content {

  @OneToOne
  private User user;

  @OneToOne
  private Group group;

  @org.hibernate.annotations.Type(type = "role")
  @Columns(columns = {@Column(name = "roleType"), @Column(name = "roleName")})
  private Role role;

  @Column
  private String comment;

  public String getComment() {
    return comment;
  }

  public Group getGroup() {
    return group;
  }

  public Role getRole() {
    return role;
  }

  public User getUser() {
    return user;
  }

  public void setComment(final String comment) {
    this.comment = comment;
  }

  public void setGroup(final Group group) {
    this.group = group;
  }

  public void setRole(final Role role) {
    this.role = role;
  }

  public void setUser(final User user) {
    this.user = user;
  }

}
