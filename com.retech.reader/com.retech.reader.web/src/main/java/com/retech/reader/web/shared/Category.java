package com.retech.reader.web.shared;

import com.goodow.web.core.shared.Content;

import com.google.inject.Inject;

import com.retech.reader.web.jpa.JpaCategoryService;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Category extends Content {

  private String title;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
  private List<Issue> issues = new ArrayList<Issue>();

  @Inject
  private transient JpaCategoryService service;

  public int getCount() {
    return service.count(this).intValue();
  }

  public List<Issue> getIssues() {
    return issues;
  }

  public String getTitle() {
    return title;
  }

  public Category setIssues(final List<Issue> issue) {
    this.issues = issue;
    return this;
  }

  public Category setTitle(final String title) {
    this.title = title;
    return this;
  }

}
