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
package com.goodow.web.view.wave.client.shell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.cellview.client.CellList;

import java.util.logging.Logger;

public class WaveShellResources {
  public static interface CellListResources extends CellList.Resources {
    @Override
    @Source({CellList.Style.DEFAULT_CSS, "WaveCellList.css"})
    CellList.Style cellListStyle();
  }

  interface Bundle extends ClientBundle {
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource bodyBackgroundTop();

    @NotStrict
    @Source("waveClean.css")
    CssResource clean();

  }

  private static Bundle INSTANCE;
  private static Logger logger = Logger.getLogger(WaveShellResources.class.getName());

  static {
    logger.finest("static init start");

    INSTANCE = GWT.create(Bundle.class);
    INSTANCE.clean().ensureInjected();

    logger.finest("static init end");
  }

  public static Bundle image() {
    return INSTANCE;
  }
}
