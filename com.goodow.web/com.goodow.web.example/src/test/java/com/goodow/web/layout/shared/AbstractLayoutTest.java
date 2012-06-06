package com.goodow.web.layout.shared;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.RequestTransport;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public class AbstractLayoutTest extends Assert {
  @Inject
  EventBus eventBus;

  @Inject
  RequestTransport requestTransport;

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }
}
