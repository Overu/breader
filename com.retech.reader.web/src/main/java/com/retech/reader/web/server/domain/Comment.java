package com.retech.reader.web.server.domain;

import org.cloudlet.web.service.server.jpa.BaseDomain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Comment extends BaseDomain {

  private String mac;

  private String text;

  @ManyToOne
  private Issue issue;

  private Date date;

  public Date getDate() {
    return date;
  }

  public Issue getIssue() {
    return issue;
  }

  public String getMac() {
    return mac;
  }

  public String getText() {
    return text;
  }

  public Comment setDate(final Date date) {
    this.date = date;
    return this;
  }

  public Comment setIssue(final Issue issue) {
    this.issue = issue;
    return this;
  }

  public Comment setMac(final String mac) {
    this.mac = mac;
    return this;
  }

  public Comment setText(final String text) {
    this.text = text;
    return this;
  }

}
