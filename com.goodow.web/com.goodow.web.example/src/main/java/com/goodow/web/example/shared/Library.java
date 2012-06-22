package com.goodow.web.example.shared;

import com.goodow.web.core.shared.WebEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_library")
public class Library extends WebEntity {

  protected String title;

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }
}
