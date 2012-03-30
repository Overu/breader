package com.retech.reader.web.server.service;

import com.retech.reader.web.server.domain.Category;
import com.retech.reader.web.server.domain.Issue;

import org.cloudlet.web.service.server.jpa.BaseService;

public class CategoryService extends BaseService<Category> {
  
  public Category findCategoryByIssue(final Issue issue) {
    return issue.getCategory();
  }

  // @Finder(query = "select d from com.retech.reader.web.server.domain.Category where d.id=?",
  // returnAs = ArrayList.class)
  // public Category find(@FirstResult final int id) {
  // throw new AssertionError();
  // }
}
