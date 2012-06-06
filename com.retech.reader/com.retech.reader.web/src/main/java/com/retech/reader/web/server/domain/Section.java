package com.retech.reader.web.server.domain;

import com.goodow.web.security.shared.Content;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Section extends Content implements HasResource {

  private String title;

  private int sequence;

  @OneToMany(mappedBy = "section")
  private List<Page> pages;

  @ManyToOne
  private Issue issue;

  @Override
  public void becomeLightWeight() {
    if (pages != null) {
      for (Page p : pages) {
        p.becomeLightWeight();
      }
    }
  }

  // public void delete() {
  // for (Page page : getPages()) {
  // page.delete();
  // }
  // super.delete();
  // }

  public Issue getIssue() {
    return issue;
  }

  public int getPageCount() {
    return pages.size();
  }

  public List<Page> getPages() {
    return pages;
  }

  public int getSequence() {
    return sequence;
  }

  public String getTitle() {
    return title;
  }

  public Section setIssue(final Issue issue) {
    this.issue = issue;
    return this;
  }

  public Section setPages(final List<Page> pages) {
    this.pages = pages;
    return this;
  }

  public Section setSequence(final int sequence) {
    this.sequence = sequence;
    return this;
  }

  public Section setTitle(final String title) {
    this.title = title;
    return this;
  }

}
