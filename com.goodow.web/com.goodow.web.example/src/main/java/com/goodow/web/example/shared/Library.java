package com.goodow.web.example.shared;

import com.goodow.web.core.shared.Content;
import com.goodow.web.core.shared.EntityType;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_library")
public class Library extends Content {

  protected String title;

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  @Override
  @Generated("")
  public EntityType type() {
    return ExamplePackage.Library.as();
  }

}
