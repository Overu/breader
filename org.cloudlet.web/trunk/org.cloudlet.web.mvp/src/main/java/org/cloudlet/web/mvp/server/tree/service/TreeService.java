package org.cloudlet.web.mvp.server.tree.service;

import com.google.inject.persist.Transactional;
import com.google.inject.persist.finder.Finder;
import com.google.inject.persist.finder.FirstResult;
import com.google.inject.persist.finder.MaxResults;

import org.cloudlet.web.mvp.server.tree.domain.TreeNode;
import org.cloudlet.web.service.server.Sort;
import org.cloudlet.web.service.server.jpa.BaseService;
import org.cloudlet.web.service.shared.SortPredicateProxy;

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
