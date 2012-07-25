/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.goodow.web.core.shared;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_section")
public class Section extends WebEntity {

  private String title;

  private int displayOrder;

  @OneToOne
  private Resource resource;

  @Transient
  private List<Section> children;

  public List<Section> getChildren() {
    return children;
  }

  public int getDisplayOrder() {
    return displayOrder;
  }

  public Resource getResource() {
    return resource;
  }

  public String getTitle() {
    return title;
  }

  public void setChildren(final List<Section> children) {
    this.children = children;
  }

  public void setDisplayOrder(final int index) {
    this.displayOrder = index;
  }

  public void setResource(final Resource resource) {
    this.resource = resource;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

}
