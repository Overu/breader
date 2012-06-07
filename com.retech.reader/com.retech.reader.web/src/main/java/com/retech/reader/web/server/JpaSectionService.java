package com.retech.reader.web.server;

import com.goodow.web.security.server.ContentServiceImpl;

import com.google.inject.persist.finder.Finder;
import com.google.inject.persist.finder.FirstResult;
import com.google.inject.persist.finder.MaxResults;

import com.retech.reader.web.shared.Issue;
import com.retech.reader.web.shared.Page;
import com.retech.reader.web.shared.Section;
import com.retech.reader.web.shared.SectionService;

import java.util.ArrayList;
import java.util.List;

public class JpaSectionService extends ContentServiceImpl<Section> implements SectionService {

  @Override
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
  @Override
  @Finder(query = "select d from com.retech.reader.web.server.domain.Page d"
      + "where d.section = :section " + "and d.section.sequence = 1 ")
  public Page findFirstPage(final Section section) {
    throw new AssertionError();
  }

  @Override
  public List<Section> findSectionByPage(final Page page) {
    return page.getSection().getIssue().getSections();
  }
}
