package com.retech.reader.web.server.service;

import com.goodow.web.core.server.BaseService;

import com.google.inject.persist.finder.Finder;
import com.google.inject.persist.finder.FirstResult;
import com.google.inject.persist.finder.MaxResults;

import com.retech.reader.web.server.domain.Issue;
import com.retech.reader.web.server.domain.Page;
import com.retech.reader.web.server.domain.Section;

import java.util.ArrayList;
import java.util.List;

public class SectionService extends BaseService<Section> {

  @Finder(query = "select d from com.retech.reader.web.server.domain.Section d where d.issue = ?", returnAs = ArrayList.class)
  public List<Section> findByBook(final Issue book, @FirstResult final int start,
      @MaxResults final int length) {
    throw new AssertionError();
  }

  // @Finder(query = "select d from com.retech.reader.web.server.domain.Section d", returnAs =
  // ArrayList.class)
  // public List<Section> find(@FirstResult final int start, @MaxResults final int length) {
  // return null;
  // }
  @Finder(query = "select d from com.retech.reader.web.server.domain.Page d"
      + "where d.section = :section " + "and d.section.sequence = 1 ")
  public Page findFirstPage(final Section section) {
    throw new AssertionError();
  }

  public List<Section> findSectionByPage(final Page page) {
    return page.getSection().getIssue().getSections();
  }
}
