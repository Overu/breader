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
package com.goodow.web.feature.client;

public class FeatureDetection {
  public static enum DevicePlatform {
    Android, iOS, Desktop,
  }

  public static boolean connectionOffline() {
    if (!mobileNative()) {
      return connectionOfflineWeb();
    } else {
      String connectionType = connectionTypeCordova();
      return connectionType != null && "none".equals(connectionType.toLowerCase());
    }
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

  public static native void hideAddressBar() /*-{
                                             $wnd.scrollTo(0, 1);
                                             }-*/;

  public static native boolean mobileNative() /*-{
                                              return !(typeof($wnd.cordova) == "undefined") || !(typeof($wnd.Cordova) == "undefined");
                                              }-*/;

  private static native boolean connectionOfflineWeb() /*-{
                                                       return !$wnd.navigator.onLine;
                                                       }-*/;

  private static native String connectionTypeCordova() /*-{
                                                         $wnd.navigator.network.connection.type;
                                                         }-*/;

  private static native String devicePlatformCordova()/*-{
                                                      return $wnd.device.platform;
                                                      }-*/;
}
