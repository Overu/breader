package com.retech.reader.web.shared.rpc;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;

import com.retech.reader.web.server.service.SectionService;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.SectionProxy;

import org.cloudlet.web.service.shared.rpc.BaseContext;

import java.util.List;

@Service(value = SectionService.class, locator = RequestFactoryLocator.class)
public interface SectionContext extends BaseContext {
  Request<List<SectionProxy>> findByBook(IssueProxy book, int start, int length);

  Request<List<SectionProxy>> findSectionByPage(PageProxy page);

  // Request<PageProxy> findFirstPage(SectionProxy section);
}
