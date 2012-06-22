package com.goodow.web.core.shared;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_organization")
public class Organization extends Group implements Principal {

  protected String url;

}
