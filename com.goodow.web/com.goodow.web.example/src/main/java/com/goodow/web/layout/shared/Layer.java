package com.goodow.web.layout.shared;

import com.goodow.web.core.shared.Content;
import com.goodow.web.core.shared.EntityType;
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
public class Layer extends Content {

  private String title;

  private LayoutStyle style;

  @ManyToOne
  private Layer parent;

  private Direction direction;

  private String widgetId;

  @OneToOne
  private Page page;

  @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
  private List<Layer> children;

  public List<Layer> getChildren() {
    return children;
  }

  public Direction getDirection() {
    return direction;
  }

  public Page getPage() {
    return page;
  }

  public Layer getParent() {
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

  public Request<Layer> save() {
    return null;
  }

  public void setChildren(final List<Layer> children) {
    this.children = children;
  }

  public void setDirection(final Direction direction) {
    this.direction = direction;
  }

  public void setPage(final Page page) {
    this.page = page;
  }

  public void setParent(final Layer parent) {
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

  @Override
  public EntityType type() {
    return null;
  }

}
