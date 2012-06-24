package com.goodow.web.layout.shared;

import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.WebEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_page")
public class Page extends WebEntity {

  private String path;

  private Page parent;

  private Layer layer;

  private String title;

  private int status;

  private String summary;

  public Layer getLayer() {
    return layer;
  }

  public Page getParent() {
    return parent;
  }

  public String getPath() {
    return path;
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

  public String getUri() {
    if (getParent() == null) {
      return "";
    }
    return getUriBuilder().toString();
  }

  public StringBuilder getUriBuilder() {
    if (getParent() == null) {
      return new StringBuilder();
    }
    return ((Page) getParent()).getUriBuilder().append("/").append(getPath());
  }

  public Request<Page> save() {
    return null;
  }

  public void setLayer(final Layer layer) {
    this.layer = layer;
  }

  public void setParent(final Page parent) {
    this.parent = parent;
  }

  public void setPath(final String path) {
    this.path = path;
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
