package com.retech.reader.web.jpa;

import com.goodow.web.mvp.jpa.JpaBaseService;

import com.google.inject.persist.finder.Finder;

import com.retech.reader.web.shared.Category;
import com.retech.reader.web.shared.CategoryService;
import com.retech.reader.web.shared.Issue;

public class JpaCategoryService extends JpaBaseService<Category> implements CategoryService {

  @Override
  @Finder(query = "select count(d) from Issue d where d.category=?")
  public Long count(final Category category) {
    throw new AssertionError();
  }

  @Override
  public Category findCategoryByIssue(final Issue issue) {
    return issue.getCategory();
  }

  // @Finder(query = "select d from com.retech.reader.web.server.domain.Category where d.id=?",
  // returnAs = ArrayList.class)
  // public Category find(@FirstResult final int id) {
  // throw new AssertionError();
  // }
}
