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
package com.goodow.web.dev.client;

public class FeatureDetection {
  public static enum DevicePlatform {
    Android, iOS, Desktop
  }

  public static DevicePlatform devicePlatform() {
    if (!mobileNative()) {
      return DevicePlatform.Desktop;
    }
    String devicePlatform = devicePlatformCordova();
    for (DevicePlatform p : DevicePlatform.values()) {
      if (p.name().equals(devicePlatform)) {
        return p;
      }
    }
    return null;
  }

  public static native boolean mobileNative() /*-{
                                        return $wnd.cordova != undefined;
                                        }-*/;

  private static native String devicePlatformCordova()/*-{
                                                      return $wnd.device.platform;
                                                      }-*/;
}
