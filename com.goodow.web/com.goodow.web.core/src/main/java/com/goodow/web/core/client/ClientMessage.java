package com.goodow.web.core.client;

import static com.google.gwt.user.client.rpc.RpcRequestBuilder.STRONG_NAME_HEADER;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.goodow.web.core.client.json.JsonRequestBuilder;
import com.goodow.web.core.shared.Entity;
import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.Response;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window.Location;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

@Singleton
public class ClientMessage extends Message implements RequestCallback {

  @Inject
  JsonRequestBuilder builder;

  private static final String SERVER_ERROR = "Server Error";

  private static final Logger logger = Logger.getLogger(ClientMessage.class.getName());

  public static final String URL = "rpc";

  private String requestUrl = GWT.getModuleBaseURL() + URL;

  @Inject
  Provider<Response<?>> responseProvider;

  Response<?> response;

  /*
   * (non-Javadoc)
   * 
   * @see org.cloudlet.web.core.shared.Message#find(org.cloudlet.web.core.shared.EntityId)
   */
  @Override
  public Entity find(final EntityId eId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void onError(final com.google.gwt.http.client.Request request, final Throwable exception) {
    try {
      // call error handler
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
      builder.parse(activeRequest, this.response, body);
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

  // TODO
  private void restCall() {
    activeRequest = requests.pop();
    response = responseProvider.get();

    JSONObject obj = builder.serialize(activeRequest, response);
    String payload = obj.toString();

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
