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

public class ApplicationCache {
  public interface Listener {
    void updateReady();
  }

  public static native void addEventListener(Listener listener) /*-{
                                                                $wnd.applicationCache.addEventListener('updateready', function(e) {
                                                                if ($wnd.applicationCache.status == $wnd.applicationCache.UPDATEREADY) {
                                                                $wnd.applicationCache.swapCache();
                                                                listener.@com.goodow.web.feature.client.ApplicationCache.Listener::updateReady()();
                                                                }
                                                                }, false);
                                                                }-*/;

  public static native void hideAddressBar() /*-{
                                             $wnd.scrollTo(0, 1);
                                             }-*/;

  public static native void swapCache() /*-{
                                             $wnd.applicationCache.swapCache();
                                             }-*/;
}
