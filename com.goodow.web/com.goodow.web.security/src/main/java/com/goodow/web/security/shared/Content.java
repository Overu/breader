package com.goodow.web.security.shared;

import com.goodow.web.core.shared.Entity;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlType;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
@XmlType
public abstract class Content extends Entity {

  @ManyToOne
  protected User owner;

  @ManyToOne
  private Group group;

  public Group getGroup() {
    return group;
  }

  public User getOwner() {
    return owner;
  }

  public void setGroup(final Group group) {
    this.group = group;
  }

  public void setOwner(final User owner) {
    this.owner = owner;
  }

}
