package com.retech.reader.web.jpa;

import com.goodow.web.mvp.jpa.JpaBaseService;

import com.google.inject.persist.Transactional;
import com.google.inject.persist.finder.Finder;
import com.google.inject.persist.finder.FirstResult;
import com.google.inject.persist.finder.MaxResults;

import com.retech.reader.web.shared.Category;
import com.retech.reader.web.shared.Issue;
import com.retech.reader.web.shared.IssueService;
import com.retech.reader.web.shared.Page;
import com.retech.reader.web.shared.Resource;
import com.retech.reader.web.shared.Section;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JpaIssueService extends JpaBaseService<Issue> implements IssueService {

  @Override
  @Finder(query = "select d from com.retech.reader.web.server.domain.Issue d where d.category = ?", returnAs = ArrayList.class)
  public List<Issue> find(final Category category) {
    throw new AssertionError();
  }

  @Override
  @Finder(query = "select d from Issue d where d = ?", returnAs = ArrayList.class)
  public List<Issue> find(final Issue issue) {
    throw new AssertionError();
  }

  @Override
  public List<Issue> find(final Issue bookGroup, final boolean brevity) {
    List<Issue> result = find(bookGroup);
    if (brevity) {
      for (Issue issue : result) {
        issue.becomeLightWeight();
      }
    }
    return result;
  }

  @Override
  @Finder(query = "select d from Issue d where d.id = ?")
  public Issue find(final long id) {
    throw new AssertionError();
  }

  @Override
  @Finder(query = "select d from Issue d where d.category = ?", returnAs = ArrayList.class)
  public List<Issue> findByCategory(final Category category, @FirstResult final int start,
      @MaxResults final int length) {
    throw new AssertionError();
  }

  @Override
  public Issue findForDonwload(final long id) {
    Issue issue = find(id);
    issue.becomeLightWeight();
    return issue;
  }

  @Override
  @Finder(query = "select d from Issue d where d.id = ?", returnAs = ArrayList.class)
  public List<Issue> findHelpIssue(final long id) {
    throw new AssertionError();
  }

  @Override
  @Finder(query = "select p from Page p where p.section.issue.id = ?", returnAs = ArrayList.class)
  public List<Page> findPages(final long id) {
    throw new AssertionError();
  }

  @Override
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

  @Override
  @Finder(query = "select d1 from Issue d1, Issue d2 where d1.category.id = d2.category.id and d2 = ?1 and d1 != ?1", returnAs = ArrayList.class)
  public List<Issue> findRecommend(final Issue issue, @FirstResult final int start,
      @MaxResults final int length) {
    throw new AssertionError();
  }

  @Override
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
  @Override
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
  public void save(final Issue domain) {
    domain.setUpdateTime(new Date());
    if (domain.getId() == null) {

      // domain.setCreateTime(new Date());
      domain.setViewCount(0);

      super.save(domain);
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
