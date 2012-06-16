package com.retech.reader.web.shared;

import com.goodow.web.mvp.shared.BaseService;

import java.util.List;

public interface PageService extends BaseService<Page> {

  Page findFirstPageByIssue(final Issue issue);

  List<Page> findPagesBySection(final Section domain);

}
