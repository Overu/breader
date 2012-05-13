package com.retech.reader.web.service;

import com.google.inject.Inject;

import com.retech.reader.web.server.domain.Issue;
import com.retech.reader.web.server.service.AdService;
import com.retech.reader.web.server.service.IssueService;

import org.cloudlet.web.test.BaseTest;
import org.junit.Test;

import java.util.List;

public class IssueServiceTest extends BaseTest {

  @Inject
  private IssueService issueService;

  @Inject
  private AdService adService;

  @Test
  public void testFind() {
    issueService.findForDonwload(1);
    issueService.findPages(1);
  }

  @Test
  public void testFindRecommend() {
    List<Issue> list = issueService.find(0, 1);
    if (list != null && list.size() > 0) {
      Issue sample = list.get(0);
      for (Issue book : adService.findRecommend(sample, 0, 3)) {
        System.out.println(book.getImage().getDataString());
      }
    }
  }

}
