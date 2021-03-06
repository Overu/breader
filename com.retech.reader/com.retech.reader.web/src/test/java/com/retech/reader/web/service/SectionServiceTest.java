package com.retech.reader.web.service;

import com.goodow.wave.test.BaseTest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.RequestTransport;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

import com.retech.reader.web.jpa.JpaPageService;
import com.retech.reader.web.jpa.JpaSectionService;
import com.retech.reader.web.shared.Page;
import com.retech.reader.web.shared.Section;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

public class SectionServiceTest extends BaseTest {
  @Inject
  JpaSectionService service;
  @Inject
  JpaPageService pageService;
  @Inject
  Provider<EntityManager> em;
  @Inject
  Provider<Page> pages;
  @Inject
  Provider<Section> sections;

  @Inject
  EventBus eventBus;
  @Inject
  RequestTransport requestTransport;
  ReaderFactory f;

  @Before
  public void resetData() {

  }

  @Before
  public void setUp() {
    f = RequestFactorySource.create(ReaderFactory.class);
    f.initialize(eventBus, requestTransport);
  }

  @Test
  public void testFindByBook() {
  }

  @Test
  public void testFindFirstPage() {
    Page page1 = pages.get();
    page1.setPageNum(1);
    Page page2 = pages.get();
    page2.setPageNum(2);
    Section section = sections.get();

  }
}
