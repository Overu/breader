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
package com.retech.reader.web.client.topbar;

import com.google.gwt.resources.client.ImageResource;

public class LabsIconDecorator {

  private ImageResource image;

  private String title;

  private Class className;

  public LabsIconDecorator(final ImageResource image, final String title, final Class className) {
    this.image = image;
    this.title = title;
    this.className = className;
  }

  public Class getClassName() {
    return className;
  }

  public ImageResource getImage() {
    return image;
  }

  public String getTitle() {
    return title;
  }

  public void setClassName(final Class className) {
    this.className = className;
  }

  public void setImage(final ImageResource image) {
    this.image = image;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

}
