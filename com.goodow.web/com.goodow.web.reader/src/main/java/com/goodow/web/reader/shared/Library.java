package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebContent;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlRootElement
@Entity
@Table(name = "t_library")
public class Library extends WebContent {

  protected String title;

  @Transient
  private List<Book> books;

  public List<Book> getBooks() {
    return books;
  }

  public String getTitle() {
    return title;
  }

  public void setBooks(final List<Book> books) {
    this.books = books;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

}
