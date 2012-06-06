package com.retech.reader.web.server.service;

import com.goodow.web.core.server.ServerContentService;

import com.google.inject.persist.Transactional;
import com.google.inject.persist.finder.Finder;
import com.google.inject.persist.finder.FirstResult;
import com.google.inject.persist.finder.MaxResults;

import com.retech.reader.web.server.domain.Category;
import com.retech.reader.web.server.domain.Issue;
import com.retech.reader.web.server.domain.Page;
import com.retech.reader.web.server.domain.Resource;
import com.retech.reader.web.server.domain.Section;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IssueService extends ServerContentService<Issue> {

  @Finder(query = "select d from com.retech.reader.web.server.domain.Issue d where d.category = ?", returnAs = ArrayList.class)
  public List<Issue> find(final Category category) {
    throw new AssertionError();
  }

  @Finder(query = "select d from Issue d where d = ?", returnAs = ArrayList.class)
  public List<Issue> find(final Issue issue) {
    throw new AssertionError();
  }

  public List<Issue> find(final Issue bookGroup, final boolean brevity) {
    List<Issue> result = find(bookGroup);
    if (brevity) {
      for (Issue issue : result) {
        issue.becomeLightWeight();
      }
    }
    return result;
  }

  @Finder(query = "select d from Issue d where d.id = ?")
  public Issue find(final long id) {
    throw new AssertionError();
  }

  @Finder(query = "select d from Issue d where d.category = ?", returnAs = ArrayList.class)
  public List<Issue> findByCategory(final Category category, @FirstResult final int start,
      @MaxResults final int length) {
    throw new AssertionError();
  }

  public Issue findForDonwload(final long id) {
    Issue issue = find(id);
    issue.becomeLightWeight();
    return issue;
  }

  @Finder(query = "select d from Issue d where d.id = ?", returnAs = ArrayList.class)
  public List<Issue> findHelpIssue(final long id) {
    throw new AssertionError();
  }

  @Finder(query = "select p from Page p where p.section.issue.id = ?", returnAs = ArrayList.class)
  public List<Page> findPages(final long id) {
    throw new AssertionError();
  }

  public List<Page> findPages(final long id, final boolean brevity) {
    List<Page> result = findPages(id);
    if (brevity) {
      for (Page page : result) {
        page.becomeLightWeight();
      }
    }
    return result;
  }

  /**
   * 相关推荐！！！
   */

  @Finder(query = "select d1 from Issue d1, Issue d2 where d1.category.id = d2.category.id and d2 = ?1 and d1 != ?1", returnAs = ArrayList.class)
  public List<Issue> findRecommend(final Issue issue, @FirstResult final int start,
      @MaxResults final int length) {
    throw new AssertionError();
  }

  public List<Issue> findRecommend(final Issue issue, @FirstResult final int start,
      @MaxResults final int length, final boolean brevity) {
    List<Issue> result = findRecommend(issue, start, length);
    if (brevity) {
      for (Issue is : result) {
        is.becomeLightWeight();
      }
    }
    return result;
  }

  /**
   * 按顺序传，注意顺序！！！
   * 
   * @param id
   * @return
   */
  public List<String> getAllResourceDataString(final long id) {
    List<String> l = new ArrayList<String>();
    Issue b = find(id);
    l.add(b.getImage().getDataString());
    for (Section c : b.getSections()) {
      for (Page p : c.getPages()) {
        for (Resource r : p.getResources()) {
          l.add(r.getDataString());
        }
      }
    }
    return l;
  }

  @Override
  @Transactional
  public void put(final Issue domain) {
    domain.setUpdateTime(new Date());
    if (domain.getId() == null) {

      // domain.setCreateTime(new Date());
      domain.setViewCount(0);

      super.put(domain);
    }
  }

  @Transactional
  @Override
  public void remove(final Issue domain) {
    for (Section section : domain.getSections()) {
      em.get().remove(section);
    }
    super.remove(domain);
  }

}
