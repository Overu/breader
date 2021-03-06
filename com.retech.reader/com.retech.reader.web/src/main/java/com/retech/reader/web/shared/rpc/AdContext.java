package com.retech.reader.web.shared.rpc;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import com.retech.reader.web.jpa.AdService;
import com.retech.reader.web.shared.proxy.IssueProxy;


import java.util.List;

@Service(value = AdService.class, locator = RequestFactoryLocator.class)
public interface AdContext extends RequestContext {

  Request<List<IssueProxy>> findRecommend(int start, int length, boolean brevity);

  Request<List<IssueProxy>> findRecommend(IssueProxy issue, int start, int length);

  Request<List<IssueProxy>> findRecommend(IssueProxy issue, int start, int length, boolean brevity);

}
