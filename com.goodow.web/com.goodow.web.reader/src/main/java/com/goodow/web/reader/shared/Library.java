package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebContent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlRootElement
@Entity
@Table(name = "t_library")
public class Library extends WebContent {

  protected String title;

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

}
