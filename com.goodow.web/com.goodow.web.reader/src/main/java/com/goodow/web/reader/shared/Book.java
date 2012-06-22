package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_book")
public class Book extends WebEntity {

}
