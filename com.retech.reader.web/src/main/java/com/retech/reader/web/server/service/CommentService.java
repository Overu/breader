package com.retech.reader.web.server.service;

import com.google.inject.persist.Transactional;
import com.google.inject.persist.finder.Finder;
import com.google.inject.persist.finder.FirstResult;
import com.google.inject.persist.finder.MaxResults;

import com.retech.reader.web.server.domain.Comment;
import com.retech.reader.web.server.domain.Issue;

import org.cloudlet.web.service.server.jpa.BaseService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentService extends BaseService<Comment> {

  @Finder(query = "select c from Comment c where c.issue.id = ?", returnAs = ArrayList.class)
  public List<Comment> find(final long issueId, @FirstResult final int start,
      @MaxResults final int length) {
    throw new AssertionError();
  }

  @Transactional
  public void put(final String mac, final String text, final long issueId) {
    Comment comment = new Comment();
    comment.setMac(mac);
    comment.setText(text);
    comment.setIssue(em.get().find(Issue.class, issueId));
    comment.setDate(new Date());
    put(comment);
  }

}
