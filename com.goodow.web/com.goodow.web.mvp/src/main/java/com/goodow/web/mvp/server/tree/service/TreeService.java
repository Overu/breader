package com.goodow.web.mvp.server.tree.service;

import com.goodow.web.core.server.BaseService;
import com.goodow.web.mvp.server.tree.domain.TreeNode;
import com.goodow.web.service.server.Sort;
import com.goodow.web.service.shared.SortPredicateProxy;

import com.google.inject.persist.Transactional;
import com.google.inject.persist.finder.Finder;
import com.google.inject.persist.finder.FirstResult;
import com.google.inject.persist.finder.MaxResults;

import java.util.ArrayList;
import java.util.List;

public class TreeService extends BaseService<TreeNode> {

  @SuppressWarnings("unused")
  @Transactional
  @Finder(query = "select t from TreeNode t" + SortPredicateProxy.SORT, returnAs = ArrayList.class)
  public List<TreeNode> find(@FirstResult final int start, @MaxResults final int length,
      @Sort final String sort) {
    throw new AssertionError();
  }

  @Override
  public void put(final TreeNode domain) {
    super.put(domain);
  }
}
