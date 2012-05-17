package com.retech.reader.web.shared.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import com.retech.reader.web.server.domain.Category;

import org.cloudlet.web.service.server.NoLocator;
import org.cloudlet.web.service.shared.rpc.BaseEntityProxy;

@ProxyFor(value = Category.class, locator = NoLocator.class)
public interface CategoryProxy extends BaseEntityProxy {

  String CATEGORY = "category.all";
  String CATEGORY_NAME = "分类";

  int getCount();

  String getTitle();

  CategoryProxy setTitle(String title);

  @Override
  EntityProxyId<CategoryProxy> stableId();

}
