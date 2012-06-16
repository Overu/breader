package com.retech.reader.web.jpa;

import com.goodow.web.mvp.jpa.JpaBaseService;

import com.google.inject.persist.finder.Finder;

import com.retech.reader.web.shared.Issue;
import com.retech.reader.web.shared.Page;
import com.retech.reader.web.shared.PageService;
import com.retech.reader.web.shared.Section;

import java.util.List;

public class JpaPageService extends JpaBaseService<Page> implements PageService {

  @Override
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

  @Override
  public List<Page> findPagesBySection(final Section domain) {
    return domain.getPages();
  }

}
