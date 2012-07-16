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
package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.WebEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Section extends WebEntity {

  @XmlElement
  private String title;

  @XmlElement
  private String body;

  private Resource resource;

  private Section parent;

  /**
   * @return the body
   */
  public String getBody() {
    return body;
  }

  /**
   * @return the resource
   */
  public Resource getResource() {
    return resource;
  }

  /**
   * @return the parent
   */
  public Section getParent() {
    return parent;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param body the body to set
   */
  public void setBody(final String body) {
    this.body = body;
  }

  /**
   * @param resource the resource to set
   */
  public void setResource(final Resource media) {
    this.resource = media;
  }

  /**
   * @param parent the parent to set
   */
  public void setParent(final Section parent) {
    this.parent = parent;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(final String title) {
    this.title = title;
  }

}
