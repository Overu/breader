package com.retech.reader.web.shared.proxy;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.service.shared.rpc.BaseEntityProxy;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import com.retech.reader.web.server.domain.Category;


@ProxyFor(value = Category.class, locator = RequestFactoryLocator.class)
public interface CategoryProxy extends BaseEntityProxy {

  String CATEGORY = "category.all";
  String CATEGORY_NAME = "网上书店";

  int getCount();

  String getTitle();

  CategoryProxy setTitle(String title);

  @Override
  EntityProxyId<CategoryProxy> stableId();

}
