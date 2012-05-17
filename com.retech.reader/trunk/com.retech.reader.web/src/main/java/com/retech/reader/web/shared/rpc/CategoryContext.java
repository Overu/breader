package com.retech.reader.web.shared.rpc;

import com.goodow.wave.server.rf.RfLocator;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;

import com.retech.reader.web.server.service.CategoryService;
import com.retech.reader.web.shared.proxy.CategoryProxy;
import com.retech.reader.web.shared.proxy.IssueProxy;

import org.cloudlet.web.service.shared.rpc.BaseContext;

import java.util.List;

@Service(value = CategoryService.class, locator = RfLocator.class)
public interface CategoryContext extends BaseContext {

  Request<List<CategoryProxy>> find(int start, int length);

  Request<CategoryProxy> findCategoryByIssue(IssueProxy issue);

}
