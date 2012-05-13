package com.retech.reader.web.shared.rpc;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;

import com.retech.reader.web.server.service.PageService;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.SectionProxy;

import org.cloudlet.web.service.server.NoLocator;
import org.cloudlet.web.service.shared.rpc.BaseContext;

import java.util.List;

@Service(value = PageService.class, locator = NoLocator.class)
public interface PageContext extends BaseContext {

  Request<PageProxy> findFirstPageByIssue(IssueProxy issue);

  Request<List<PageProxy>> findPagesBySection(SectionProxy section);

}
