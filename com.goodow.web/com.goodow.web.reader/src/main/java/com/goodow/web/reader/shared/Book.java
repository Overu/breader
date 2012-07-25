package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.Category;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.Section;
import com.goodow.web.core.shared.WebEntity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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

  @Transient
  private List<Resource> resources;

  @Transient
  private List<Section> sections;

  @ManyToOne
  private Category category;

  public String getAuthor() {
    return author;
  }

  public Category getCategory() {
    return category;
  }

  public Resource getCover() {
    return cover;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public Date getDateModified() {
    return dateModified;
  }

  public String getDescription() {
    return description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public List<Resource> getResources() {
    return resources;
  }

  public List<Section> getSections() {
    return sections;
  }

  public Resource getSource() {
    return source;
  }

  public String getTitle() {
    return title;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setAuthor(final String author) {
    this.author = author;
  }

  public void setCategory(final Category category) {
    this.category = category;
  }

  public void setCover(final Resource cover) {
    this.cover = cover;
  }

  public void setDateCreated(final Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public void setDateModified(final Date dateModified) {
    this.dateModified = dateModified;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public void setImageUrl(final String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setResources(final List<Resource> resources) {
    this.resources = resources;
  }

  public void setSections(final List<Section> sections) {
    this.sections = sections;
  }

  public void setSelected(final boolean selected) {
    this.selected = selected;
  }

  public void setSource(final Resource resource) {
    this.source = resource;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

}
