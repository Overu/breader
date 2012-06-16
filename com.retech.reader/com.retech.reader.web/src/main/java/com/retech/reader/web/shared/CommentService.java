package com.retech.reader.web.shared;

import com.goodow.web.mvp.shared.BaseService;

import com.google.inject.persist.finder.FirstResult;
import com.google.inject.persist.finder.MaxResults;

import java.util.List;

public interface CommentService extends BaseService<Comment> {

  List<Comment> find(final long issueId, @FirstResult final int start, @MaxResults final int length);

  void put(final String mac, final String text, final long issueId);

}
