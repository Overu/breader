package com.goodow.web.core.shared;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_page")
public class PageConfig extends UIConfig {

  private PageConfig parent;

  private ViewConfig layer;

  private String title;

  private int status;

  private String summary;

  public ViewConfig getLayer() {
    return layer;
  }

  public PageConfig getParent() {
    return parent;
  }

  public int getStatus() {
    return status;
  }

  public String getSummary() {
    return summary;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public String getUri() {
    if (getParent() == null) {
      return "";
    }
    return getUriBuilder().toString();
  }

  @Override
  public StringBuilder getUriBuilder() {
    if (getParent() == null) {
      return new StringBuilder();
    }
    return ((PageConfig) getParent()).getUriBuilder().append("/").append(getPath());
  }

  public Request<PageConfig> save() {
    return null;
  }

  public void setLayer(final ViewConfig layer) {
    this.layer = layer;
  }

  public void setParent(final PageConfig parent) {
    this.parent = parent;
  }

  public void setStatus(final int status) {
    this.status = status;
  }

  public void setSummary(final String summary) {
    this.summary = summary;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

}
