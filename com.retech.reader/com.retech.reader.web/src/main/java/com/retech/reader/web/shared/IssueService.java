package com.retech.reader.web.shared;

import com.goodow.web.security.shared.ContentService;

import com.google.inject.persist.finder.FirstResult;
import com.google.inject.persist.finder.MaxResults;

import java.util.List;

public interface IssueService extends ContentService<Issue> {

  List<Issue> find(final Category category);

  List<Issue> find(final Issue issue);

  List<Issue> find(final Issue bookGroup, final boolean brevity);

  Issue find(final long id);

  List<Issue> findByCategory(final Category category, @FirstResult final int start,
      @MaxResults final int length);

  Issue findForDonwload(final long id);

  List<Issue> findHelpIssue(final long id);

  List<Page> findPages(final long id);

  List<Page> findPages(final long id, final boolean brevity);

  List<Issue> findRecommend(final Issue issue, @FirstResult final int start,
      @MaxResults final int length);

  List<Issue> findRecommend(final Issue issue, @FirstResult final int start,
      @MaxResults final int length, final boolean brevity);

  List<String> getAllResourceDataString(final long id);

  void put(final Issue domain);

  void remove(final Issue domain);
}
