package com.goodow.web.core.client;

import static com.google.gwt.user.client.rpc.RpcRequestBuilder.STRONG_NAME_HEADER;

import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.WebContent;
import com.goodow.web.core.shared.WebType;
import com.goodow.web.feature.client.FeatureDetection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window.Location;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import java.util.logging.Level;

public class ClientMessage extends Message implements RequestCallback {

  @Inject
  ClientJSONMarshaller messageProvider;

  @Override
  public WebContent find(final ObjectType objectType, final String id) {
    return null;
  }

  @Override
  public Message fire() {
    try {
      JSONObject obj = messageProvider.serialize(this);
      String payload = obj.toString();

      String requestUrl =
          (FeatureDetection.mobileNative() ? "http://retechlive.xicp.net/" + GWT.getModuleName()
              + "/" : GWT.getModuleBaseURL())
              + URL;
      RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, requestUrl);
      configureRequestBuilder(builder);
      builder.setRequestData(payload);
      builder.setCallback(this);
      logger.finest("Request: " + payload);
      builder.send();
    } catch (RequestException e) {
      logger.log(Level.SEVERE, SERVER_ERROR + " (" + e.getMessage() + ")", e);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Serialization Error (" + e.getMessage() + ")", e);
    }
    return this;
  }

  @Override
  public void onError(final com.google.gwt.http.client.Request request, final Throwable exception) {
    logger.finest("onError");
  }

  @Override
  public void onResponseReceived(final com.google.gwt.http.client.Request request,
      final com.google.gwt.http.client.Response response) {
    try {
      String body = response.getText();
      logger.finest("Response: " + body);
      JSONValue obj = JSONParser.parse(body);
      WebType type = this.request.getOperation().getType();
      Object result = messageProvider.parse(type, obj, this);
      this.response.setResult(result);
      Receiver r = this.request.getReceiver();
      if (r != null) {
        r.onSuccess(this.response.getResult());
      }
    } catch (Exception e) {
      logger.severe(e.getMessage());
    }
  }

  protected void configureRequestBuilder(final RequestBuilder builder) {
    builder.setHeader("Content-Type", RequestFactory.JSON_CONTENT_TYPE_UTF8);
    builder.setHeader("pageurl", Location.getHref());
    builder.setHeader(STRONG_NAME_HEADER, GWT.getPermutationStrongName());
  }

}
