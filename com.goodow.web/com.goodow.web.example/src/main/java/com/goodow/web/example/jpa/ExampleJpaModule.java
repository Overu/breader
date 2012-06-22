package com.goodow.web.example.jpa;

import com.goodow.web.core.jpa.JpaModule;
import com.goodow.web.example.shared.LibraryService;

import com.google.inject.Singleton;

import java.util.logging.Logger;

@Singleton
public class ExampleJpaModule extends JpaModule {

  private final Logger logger = Logger.getLogger(getClass().getName());

  @java.lang.Override
  protected void configure() {
    logger.finest("Install JpaServiceModule begin");
    bind(LibraryService.class).to(JpaLibraryService.class);
    logger.finest("Install JpaServiceModule end");
  }
}