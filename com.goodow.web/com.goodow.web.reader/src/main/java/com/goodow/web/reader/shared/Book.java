package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.WebEntity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
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

  private Date dateCreated;

  private Date dateModified;

  private boolean selected;

  @OneToOne
  private Resource source;

  @OneToOne
  private Resource cover;

  /**
   * @return the author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * @return the cover
   */
  public Resource getCover() {
    return cover;
  }

  /**
   * @return the dateCreated
   */
  public Date getDateCreated() {
    return dateCreated;
  }

  /**
   * @return the dateModified
   */
  public Date getDateModified() {
    return dateModified;
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
   * @return the source
   */
  public Resource getSource() {
    return source;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @return the selected
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * @param author the author to set
   */
  public void setAuthor(final String author) {
    this.author = author;
  }

  /**
   * @param cover the cover to set
   */
  public void setCover(final Resource cover) {
    this.cover = cover;
  }

  /**
   * @param dateCreated the dateCreated to set
   */
  public void setDateCreated(final Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  /**
   * @param dateModified the dateModified to set
   */
  public void setDateModified(final Date dateModified) {
    this.dateModified = dateModified;
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
   * @param selected the selected to set
   */
  public void setSelected(final boolean selected) {
    this.selected = selected;
  }

  /**
   * @param source the source to set
   */
  public void setSource(final Resource resource) {
    this.source = resource;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(final String title) {
    this.title = title;
  }

}
