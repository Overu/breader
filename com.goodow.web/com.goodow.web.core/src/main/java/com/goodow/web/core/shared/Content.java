package com.goodow.web.core.shared;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlType;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
@XmlType
public class Content extends Entity {

  @ManyToOne
  protected User owner;

  @ManyToOne
  private Group ownerGroup;

  public User getOwner() {
    return owner;
  }

  public Group getOwnerGroup() {
    return ownerGroup;
  }

  public void setOwner(final User owner) {
    this.owner = owner;
  }

  public void setOwnerGroup(final Group group) {
    this.ownerGroup = group;
  }

}
