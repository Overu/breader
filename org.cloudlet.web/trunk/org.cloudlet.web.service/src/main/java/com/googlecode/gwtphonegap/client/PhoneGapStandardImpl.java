/*
 * Copyright 2010 Daniel Kurka
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
package com.googlecode.gwtphonegap.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Timer;

import com.googlecode.gwtphonegap.client.file.File;

public class PhoneGapStandardImpl implements PhoneGap {

  private static final int INIT_TICK = 10;

  private File file;
  private EventBus handlerManager = new SimpleEventBus();

  public PhoneGapStandardImpl() {
  }

  @Override
  public HandlerRegistration addHandler(final PhoneGapAvailableHandler handler) {
    return handlerManager.addHandler(PhoneGapAvailableEvent.TYPE, handler);
  }

  @Override
  public HandlerRegistration addHandler(final PhoneGapTimeoutHandler handler) {
    return handlerManager.addHandler(PhoneGapTimeoutEvent.TYPE, handler);
  }

  @Override
  public native boolean exitApp() /*-{
                                  if ($wnd.navigator.app != null) {
                                  if ($wnd.navigator.app.exitApp != null) {
                                  $wnd.navigator.app.exitApp();
                                  return true;
                                  }
                                  }
                                  return false;

                                  }-*/;

  @Override
  public File getFile() {
    return file;
  }

  @Override
  public void initializePhoneGap() {
    initializePhoneGap(10000);
  }

  @Override
  public void initializePhoneGap(final int timeoutInMs) {

    final long end = System.currentTimeMillis() + timeoutInMs;
    if (isPhoneGapInitialized()) {

      firePhoneGapAvailable();

    } else {
      Timer timer = new Timer() {

        @Override
        public void run() {
          if (isPhoneGapInitialized()) {
            firePhoneGapAvailable();
            return;
          }

          if (System.currentTimeMillis() - end > 0) {
            handlerManager.fireEvent(new PhoneGapTimeoutEvent());
          } else {
            schedule(INIT_TICK);
          }

        }
      };

      timer.schedule(INIT_TICK);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.googlecode.gwtphonegap.client.PhoneGap#isDevMode()
   */
  @Override
  public boolean isDevMode() {
    return !GWT.isScript();
  }

  @Override
  public native boolean isPhoneGapInitialized()/*-{
                                               //phonegap 1.5 ios
                                               if(!(typeof($wnd.Cordova) == "undefined")){
                                               return $wnd.Cordova.available;
                                               }
                                               
                                               //phonegap 1.5 android and others
                                               if(!(typeof($wnd.cordova) == "undefined")){
                                               return $wnd.cordova.available;
                                               }
                                               
                                               if (typeof ($wnd.PhoneGap) == "undefined") {
                                               return false;
                                               } else {
                                               return $wnd.PhoneGap.available;
                                               }
                                               }-*/;

  protected File constructFile() {
    return GWT.create(File.class);
  }

  protected void constructModules() {
    file = constructFile();
  }

  private void firePhoneGapAvailable() {

    constructModules();

    handlerManager.fireEvent(new PhoneGapAvailableEvent());
  }

}
