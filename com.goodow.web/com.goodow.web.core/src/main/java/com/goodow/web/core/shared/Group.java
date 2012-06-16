package com.goodow.web.core.shared;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_group")
public class Group extends Content {

  protected String name;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

}
