package com.retech.reader.web.shared;

import com.goodow.web.security.shared.Content;

import com.google.inject.Inject;

import com.retech.reader.web.server.JpaPageService;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Page extends Content implements HasResource {

  private int pageNum;
  private String title;
  private String mainResourceFilename;

  @Inject
  @Transient
  private JpaPageService pageService;

  @OneToMany(mappedBy = "page")
  private List<Resource> resources;

  @ManyToOne
  private Section section;

  @Override
  public void becomeLightWeight() {
    if (getResources() != null) {
      for (Resource r : getResources()) {
        r.becomeLightWeight();
      }
    }
  }

  public String getMainResourceFilename() {
    return mainResourceFilename;
  }

  public int getPageNum() {
    return pageNum;
  }

  public List<Resource> getResources() {
    return resources;
  }

  public Section getSection() {
    return section;
  }

  public String getTitle() {
    return title;
  }

  public Page setMainResourceFilename(final String mainResourceFilename) {
    this.mainResourceFilename = mainResourceFilename;
    return this;
  }

  public Page setPageNum(final int pageNum) {
    this.pageNum = pageNum;
    return this;
  }

  public Page setSection(final Section section) {
    this.section = section;
    return this;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

}
