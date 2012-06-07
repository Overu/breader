package com.retech.reader.web.shared;

import com.goodow.web.security.shared.ContentService;

import java.util.List;

public interface PageService extends ContentService<Page> {

  Page findFirstPageByIssue(final Issue issue);

  List<Page> findPagesBySection(final Section domain);

}
