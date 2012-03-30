package com.retech.reader.web.service;

import com.google.inject.Inject;

import com.retech.reader.web.server.domain.Page;
import com.retech.reader.web.server.domain.Resource;
import com.retech.reader.web.server.service.PageService;
import com.retech.reader.web.shared.common.SQLConstant;
import com.retech.reader.web.shared.proxy.MimeType;

import org.cloudlet.web.test.BaseTest;
import org.junit.Test;

public class PageServiceTest extends BaseTest {

  @Inject
  private PageService pageService;

  @Test
  public void testFindResources() {
    for (Page page : pageService.find(0, SQLConstant.MAX_RESULTS_ALL)) {
      for (Resource resource : page.getResources()) {
        // System.out.println(resource.getFilename());
        if (resource.getMimeType() == MimeType.HTML) {
          System.out.println(resource.getDataString());
        }
      }
    }
  }

}
