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
package com.goodow.wave.client.wavepanel.blip;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

import java.util.logging.Logger;

public class WaveBlipResources {

  public interface Style extends CssResource {
  }
  interface Bundle extends ClientBundle {
    @Source("WaveBlip.css")
    Style style();

    ImageResource waveBlipMoreActions();

    ImageResource waveBlipPopupContextEdit();

    ImageResource waveBlipPopupContextReply();

    ImageResource waveBlipReply();
  }

  private static Bundle INSTANCE;
  private static Logger logger = Logger.getLogger(WaveBlipResources.class.getName());

  static {
    logger.finest("static init start");
    INSTANCE = GWT.create(Bundle.class);
    INSTANCE.style().ensureInjected();
    logger.finest("static init end");
  }

  public static Style css() {
    return INSTANCE.style();
  }

  public static Bundle image() {
    return INSTANCE;
  }
}
