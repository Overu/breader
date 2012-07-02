package com.goodow.web.ui.shared;

import com.goodow.web.core.shared.Request;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_layer")
public class ViewConfig extends UIConfig {

  private String title;

  private LayoutStyle style;

  @ManyToOne
  private ViewConfig parent;

  private Direction direction;

  private String widgetId;

  @OneToOne
  private PageConfig page;

  @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
  private List<ViewConfig> children;

  public List<ViewConfig> getChildren() {
    return children;
  }

  public Direction getDirection() {
    return direction;
  }

  public PageConfig getPage() {
    return page;
  }

  public ViewConfig getParent() {
    return parent;
  }

  public LayoutStyle getStyle() {
    return style;
  }

  public String getTitle() {
    return title;
  }

  public String getWidgetId() {
    return widgetId;
  }

  public Request<ViewConfig> save() {
    return null;
  }

  public void setChildren(final List<ViewConfig> children) {
    this.children = children;
  }

  public void setDirection(final Direction direction) {
    this.direction = direction;
  }

  public void setPage(final PageConfig page) {
    this.page = page;
  }

  public void setParent(final ViewConfig parent) {
    this.parent = parent;
  }

  public void setStyle(final LayoutStyle style) {
    this.style = style;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public void setWidgetId(final String widgetId) {
    this.widgetId = widgetId;
  }

}
