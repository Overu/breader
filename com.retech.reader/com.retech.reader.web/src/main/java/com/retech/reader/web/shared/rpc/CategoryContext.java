package com.retech.reader.web.shared.rpc;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.mvp.shared.tree.rpc.BaseContext;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;

import com.retech.reader.web.server.CategoryServiceImpl;
import com.retech.reader.web.shared.proxy.CategoryProxy;
import com.retech.reader.web.shared.proxy.IssueProxy;


import java.util.List;

@Service(value = CategoryServiceImpl.class, locator = RequestFactoryLocator.class)
public interface CategoryContext extends BaseContext {

  Request<List<CategoryProxy>> find(int start, int length);

  Request<CategoryProxy> findCategoryByIssue(IssueProxy issue);

}
