package com.retech.reader.web.jpa;

import com.goodow.web.mvp.jpa.JpaBaseService;

import com.google.inject.persist.Transactional;
import com.google.inject.persist.finder.Finder;
import com.google.inject.persist.finder.FirstResult;
import com.google.inject.persist.finder.MaxResults;

import com.retech.reader.web.shared.Comment;
import com.retech.reader.web.shared.CommentService;
import com.retech.reader.web.shared.Issue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentServiceImpl extends JpaBaseService<Comment> implements CommentService {

  @Override
  @Finder(query = "select c from Comment c where c.issue.id = ?", returnAs = ArrayList.class)
  public List<Comment> find(final long issueId, @FirstResult final int start,
      @MaxResults final int length) {
    throw new AssertionError();
  }

  @Override
  @Transactional
  public void put(final String mac, final String text, final long issueId) {
    Comment comment = new Comment();
    comment.setMac(mac);
    comment.setText(text);
    comment.setIssue(em.get().find(Issue.class, issueId));
    comment.setDate(new Date());
    save(comment);
  }

}
