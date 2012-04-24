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
import com.google.gwt.user.client.ui.Hyperlink;

public class ImageAndHyperlink {

  private ImageResource image;

  private Hyperlink link;

  public ImageAndHyperlink(final ImageResource image, final Hyperlink link) {
    this.image = image;
    this.link = link;
  }

  public ImageResource getImage() {
    return image;
  }

  public Hyperlink getLink() {
    return link;
  }

  public void setImage(final ImageResource image) {
    this.image = image;
  }

  public void setLink(final Hyperlink link) {
    this.link = link;
  }

}
