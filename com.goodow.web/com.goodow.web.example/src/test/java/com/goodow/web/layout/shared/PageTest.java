package com.goodow.web.layout.shared;

import org.junit.Test;

import com.goodow.web.layout.shared.Page;

public class PageTest extends AbstractLayoutTest {

  private Page rootPage;

  @Test
  public void testCreatePage() {
    testGetRootPage();
    final String title = "page-" + System.currentTimeMillis();
    Page page = new Page();
    page.setParent(rootPage);
    page.setPath(title);
    page.setTitle(title);
    page.save();
  }

  @Test
  public void testGetRootPage() {

  }

}
