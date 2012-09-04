package com.goodow.web.core.shared;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_category")
public class Category extends WebContent {

  private String title;

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

}
