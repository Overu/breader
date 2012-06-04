package com.goodow.web.service.client.rpc;

import com.goodow.wave.client.widget.loading.LoadingIndicator;
import com.goodow.web.feature.client.FeatureDetection;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;

import java.util.logging.Logger;

@Singleton
public class BaseRequestTransport extends DefaultRequestTransport implements ScheduledCommand {
  private static final Logger wireLogger = Logger.getLogger("WireActivityLogger");
  private static int inProcessReqs = 0;
  private final Element loading;
  private boolean scheduled;

  @Inject
  BaseRequestTransport(final LoadingIndicator loadingIndicator) {
    loading = loadingIndicator.getElement();
    if (FeatureDetection.mobileNative()) {
      super.setRequestUrl("http://dev.goodow.com/" + DefaultRequestTransport.URL);
    }
  }

  @Override
  public void execute() {
    scheduled = false;
    if (inProcessReqs == 0) {
      loading.removeFromParent();
    } else if (inProcessReqs > 0 && loading.getParentElement() == null) {
      RootPanel.get().getElement().appendChild(loading);
    }
  }

  @Override
  public void send(final String payload, final TransportReceiver receiver) {
    if (FeatureDetection.connectionOffline()) {
      wireLogger.warning("请确保网络连接正常,然后重试");
      return;
    }
    super.send(payload, receiver);
    loadingIndicator(1);
  }

  @Override
  protected RequestCallback createRequestCallback(final TransportReceiver receiver) {
    final RequestCallback superCallback = super.createRequestCallback(receiver);

    return new RequestCallback() {
      @Override
      public void onError(final Request request, final Throwable exception) {
        loadingIndicator(-1);
        superCallback.onError(request, exception);
      }

      @Override
      public void onResponseReceived(final Request request, final Response response) {
        loadingIndicator(-1);
        superCallback.onResponseReceived(request, response);
      }
    };
  }

  private void loadingIndicator(final int num) {
    inProcessReqs += num;
    if (!scheduled) {
      scheduled = true;
      Scheduler.get().scheduleFinally(this);
    }
  }
}
