package com.retech.reader.web.service;

import com.goodow.wave.test.BaseTest;

import com.google.inject.Inject;

import com.gooodow.wave.shared.media.MimeType;
import com.retech.reader.web.jpa.JpaPageService;
import com.retech.reader.web.shared.Page;
import com.retech.reader.web.shared.Resource;
import com.retech.reader.web.shared.common.SQLConstant;

import org.junit.Test;

public class PageServiceTest extends BaseTest {

  @Inject
  private JpaPageService pageService;

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
