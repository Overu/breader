package com.goodow.web.security.shared;

import com.goodow.web.core.shared.EntityType;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity(name = "t_user")
public class User extends Content {
  @Override
  public EntityType type() {
    return SecurityPackage.User.as();
  }
}
