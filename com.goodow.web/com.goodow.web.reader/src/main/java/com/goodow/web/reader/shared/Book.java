package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_book")
public class Book extends WebEntity {

  private String title;

  private String description;

  private String author;

  private String imageUrl;

  /**
   * @return the author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return the imageUrl
   */
  public String getImageUrl() {
    return imageUrl;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param author the author to set
   */
  public void setAuthor(final String author) {
    this.author = author;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(final String description) {
    this.description = description;
  }

  /**
   * @param imageUrl the imageUrl to set
   */
  public void setImageUrl(final String imageUrl) {
    this.imageUrl = imageUrl;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(final String title) {
    this.title = title;
  }

}
