package com.retech.reader.web.server.service;

import com.google.inject.Inject;
import com.google.inject.persist.finder.Finder;
import com.google.inject.persist.finder.FirstResult;
import com.google.inject.persist.finder.MaxResults;

import com.retech.reader.web.server.domain.Issue;

import java.util.ArrayList;
import java.util.List;

public class AdService {

  @Inject
  IssueService issueService;

  public List<Issue> findRecommend(@FirstResult final int start, @MaxResults final int length,
      final boolean brevity) {
    List<Issue> result = issueService.find(start, length);
    if (brevity) {
      for (Issue issue : result) {
        issue.getImage().becomeLightWeight();
      }
    }
    return result;
  }

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

}
