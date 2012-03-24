package org.cloudlet.web.offline.client;

public class Connectivity {
  public interface Listener {
    void onOffline();

    void onOnline();
  }

  public static native void addEventListener(Listener listener) /*-{
                                                                $wnd.addEventListener("offline", function(e) {
                                                                listener.@org.cloudlet.web.offline.client.Connectivity.Listener::onOffline()();
                                                                }, false);
                                                                window.addEventListener("online", function(e) {
                                                                  listener.@org.cloudlet.web.offline.client.Connectivity.Listener::onOnline()();
                                                                }, false);
                                                                }-*/;

  public static native boolean isOnline() /*-{
                                           return $wnd.navigator.onLine;
                                           }-*/;
}
