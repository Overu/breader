package com.retech.reader.web.shared.proxy;

import com.goodow.wave.server.rf.RfLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import com.retech.reader.web.server.domain.Category;

import org.cloudlet.web.service.shared.rpc.BaseEntityProxy;

@ProxyFor(value = Category.class, locator = RfLocator.class)
public interface CategoryProxy extends BaseEntityProxy {

  String CATEGORY = "category.all";
  String CATEGORY_NAME = "网上书店";

  int getCount();

  String getTitle();

  CategoryProxy setTitle(String title);

  @Override
  EntityProxyId<CategoryProxy> stableId();

}
