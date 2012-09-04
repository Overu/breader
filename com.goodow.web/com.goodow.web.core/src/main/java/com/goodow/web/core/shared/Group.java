package com.goodow.web.core.shared;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "t_group")
public class Group extends WebContent {

  protected String name;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

}
