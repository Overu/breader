package com.goodow.web.reader.jpa;

import com.goodow.web.core.jpa.JpaModule;
import com.goodow.web.reader.shared.BookService;
import com.goodow.web.reader.shared.LibraryService;

import com.google.inject.Singleton;

import java.util.logging.Logger;

@Singleton
public class ReaderJpaModule extends JpaModule {

  private final Logger logger = Logger.getLogger(getClass().getName());

  @java.lang.Override
  protected void configure() {
    logger.finest("Install JpaServiceModule begin");
    bind(LibraryService.class).to(JpaLibraryService.class);
    bind(BookService.class).to(JpaBookService.class);
    logger.finest("Install JpaServiceModule end");
  }
}