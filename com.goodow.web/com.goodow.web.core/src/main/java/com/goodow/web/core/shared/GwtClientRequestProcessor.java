package com.goodow.web.core.shared;

import static com.google.gwt.user.client.rpc.RpcRequestBuilder.STRONG_NAME_HEADER;

import com.goodow.web.core.client.json.GwtJsonProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window.Location;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import java.util.logging.Level;

@Singleton
public class GwtClientRequestProcessor extends RequestProcessor implements RequestCallback {

  @Inject
  private GwtJsonProvider provider;

  @Override
  public void onError(final com.google.gwt.http.client.Request request, final Throwable exception) {
    try {
      logger.finest("onError");
    } finally {
      activeRequest = null;
    }
  }

  @Override
  public void onResponseReceived(final com.google.gwt.http.client.Request request,
      final com.google.gwt.http.client.Response response) {
    try {
      String body = response.getText();
      provider.parse(activeRequest, this.response, body);
      if (this.response.isSuccess()) {
        Receiver r = activeRequest.getReceiver();
        if (r != null) {
          r.onSuccess(this.response.getResult());
        }
      } else {

      }
      logger.finest("onResponseReceived");
    } finally {
      activeRequest = null;
    }
  }

  @Override
  public Response send() {
    if (!requests.isEmpty()) {

      if (isActive()) {
        Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
          @Override
          public boolean execute() {
            if (isActive()) {
              return true;
            }
            restCall();
            return false;
          }
        }, 20);
      } else {
        restCall();
      }
    }
    return response;
  }

  protected void configureRequestBuilder(final RequestBuilder builder) {
    builder.setHeader("Content-Type", RequestFactory.JSON_CONTENT_TYPE_UTF8);
    builder.setHeader("pageurl", Location.getHref());
    builder.setHeader(STRONG_NAME_HEADER, GWT.getPermutationStrongName());
  }

  private void restCall() {
    activeRequest = requests.pop();
    response = responseProvider.get();

    JSONObject obj = provider.serialize(activeRequest, response);
    String payload = obj.toString();

    String requestUrl = GWT.getModuleBaseURL() + URL;
    RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, requestUrl);
    configureRequestBuilder(builder);
    builder.setRequestData(payload);
    builder.setCallback(this);

    try {
      logger.finest("Sending fire request");
      builder.send();
    } catch (RequestException e) {
      logger.log(Level.SEVERE, SERVER_ERROR + " (" + e.getMessage() + ")", e);
    }
  }
}
