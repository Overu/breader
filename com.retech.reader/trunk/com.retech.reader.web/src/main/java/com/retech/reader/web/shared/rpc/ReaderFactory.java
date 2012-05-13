package com.retech.reader.web.shared.rpc;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface ReaderFactory extends RequestFactory {
  AdContext ad();

  CategoryContext category();

  CommentContext comment();

  IssueContext issue();

  PageContext pageContext();

  ResourceContext resource();

  SectionContext section();

  TalkContext talkContext();

}
