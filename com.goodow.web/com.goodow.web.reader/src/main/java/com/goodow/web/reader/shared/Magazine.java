package com.goodow.web.reader.shared;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_magazine")
public class Magazine extends Publication {

}
