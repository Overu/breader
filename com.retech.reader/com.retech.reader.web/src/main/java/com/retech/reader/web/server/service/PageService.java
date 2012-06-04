package com.retech.reader.web.server.service;

import com.goodow.web.service.server.jpa.BaseService;

import com.google.inject.persist.finder.Finder;

import com.retech.reader.web.server.domain.Issue;
import com.retech.reader.web.server.domain.Page;
import com.retech.reader.web.server.domain.Section;


import java.util.List;

public class PageService extends BaseService<Page> {

  @Finder(query = "select p from Page p where p.section.issue = ? and p.section.sequence = 1")
  public Page findFirstPageByIssue(final Issue issue) {
    throw new AssertionError();
  }

  // @Finder(query =
  // "select r from Resource r, Page p , PageResource pr where r.id = pr.resource.id and p.id = pr.page.id and p = ?",
  // returnAs = ArrayList.class)
  // public List<Resource> findResources(final Page page) {
  // throw new AssertionError();
  // }

  public List<Page> findPagesBySection(final Section domain) {
    return domain.getPages();
  }

}
