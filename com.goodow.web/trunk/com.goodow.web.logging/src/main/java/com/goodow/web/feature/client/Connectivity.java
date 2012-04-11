package com.goodow.web.feature.client;

public class Connectivity {
  public interface Listener {
    void onOffline();

    void onOnline();
  }

  public static native void addEventListener(Listener listener) /*-{
                                                                $wnd.addEventListener("offline", function(e) {
                                                                listener.@com.goodow.web.feature.client.Connectivity.Listener::onOffline()();
                                                                }, false);
                                                                $wnd.addEventListener("online", function(e) {
                                                                  listener.@com.goodow.web.feature.client.Connectivity.Listener::onOnline()();
                                                                }, false);
                                                                }-*/;

}
