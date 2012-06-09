package com.retech.reader.web.shared.rpc;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.mvp.shared.tree.rpc.BaseContext;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;

import com.retech.reader.web.server.JpaPageService;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.SectionProxy;


import java.util.List;

@Service(value = JpaPageService.class, locator = RequestFactoryLocator.class)
public interface PageContext extends BaseContext {

  Request<PageProxy> findFirstPageByIssue(IssueProxy issue);

  Request<List<PageProxy>> findPagesBySection(SectionProxy section);

}
