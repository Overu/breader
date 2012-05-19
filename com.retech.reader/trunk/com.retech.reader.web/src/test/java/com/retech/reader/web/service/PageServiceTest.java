package com.retech.reader.web.service;

import com.goodow.wave.server.media.MimeType;
import com.goodow.wave.test.BaseTest;

import com.google.inject.Inject;

import com.retech.reader.web.server.domain.Page;
import com.retech.reader.web.server.domain.Resource;
import com.retech.reader.web.server.service.PageService;
import com.retech.reader.web.shared.common.SQLConstant;

import org.junit.Test;

public class PageServiceTest extends BaseTest {

  @Inject
  private PageService pageService;

  @Test
  public void testFindResources() {
    for (Page page : pageService.find(0, SQLConstant.MAX_RESULTS_ALL)) {
      for (Resource resource : page.getResources()) {
        // System.out.println(resource.getFilename());
        if (resource.getMimeType() == MimeType.TEXT_HTML) {
          System.out.println(resource.getDataString());
        }
      }
    }
  }

}
