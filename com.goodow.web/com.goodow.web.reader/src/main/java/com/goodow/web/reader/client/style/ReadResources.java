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
package com.goodow.web.reader.client.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellList;

import com.googlecode.mgwt.ui.client.theme.base.ListCss;

import java.util.logging.Logger;

public class ReadResources {

  public interface Bundle extends ClientBundle {

    @Source("list.css")
    CategoryListCss categroyListCss();

    ImageResource cover();

    @Source("style.css")
    Style css();

    @Source("readHeader.css")
    ReadHeader readHeadCss();
  }

  public interface CategoryListCss extends ListCss {
    String categorContainer();
  }

  public static interface CellListResources extends CellList.Resources {
    @Override
    @Source({CellList.Style.DEFAULT_CSS, "WaveCellList.css"})
    CellListStyle cellListStyle();
  }

  public interface CellListStyle extends CellList.Style {
    String cellListBasicImformation();
  }

  public interface ReadHeader extends CssResource {
    String headPanel();
  }

  public interface Style extends CssResource {
    String topBarButtonHOVER();
  }

  private static final Logger logger = Logger.getLogger(ReadResources.class.getName());
  private static Bundle INSTANCE;
  private static CellListResources CELLLISTSTYLE;

  static {
    logger.finest("static init start");
    INSTANCE = GWT.create(Bundle.class);
    CELLLISTSTYLE = GWT.create(CellListResources.class);
    INSTANCE.css().ensureInjected();
    INSTANCE.categroyListCss().ensureInjected();
    INSTANCE.readHeadCss().ensureInjected();
    logger.finest("static init end");
  }

  public static CellListResources CELLLISTINSTANCE() {
    return CELLLISTSTYLE;
  }

  public static Bundle INSTANCE() {
    return INSTANCE;
  }

}
