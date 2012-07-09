package com.goodow.web.core.shared;

import static com.google.gwt.user.client.rpc.RpcRequestBuilder.STRONG_NAME_HEADER;

import com.goodow.web.core.client.json.GwtJsonProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window.Location;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import java.util.logging.Level;

@Singleton
public class GwtClientRequestProcessor extends RequestProcessor implements RequestCallback {

  @Inject
  private GwtJsonProvider provider;

  @Inject
  private Provider<Message> message;

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
      parse(activeRequest, this.response, body);
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

  public void parse(final Request request, final Response response, final JSONObject obj) {
    WebType type = request.getOperation().getType();
    JSONBoolean success = obj.get("success").isBoolean();
    response.setSuccess(success.booleanValue());
    if (success != null && response.isSuccess()) {
      JSONValue jsonResult = obj.get("result");
      Object result = provider.parse(type, jsonResult);
      response.setResult(result);
    } else {
      JSONObject error = obj.get("error").isObject();
    }
  }

  public void parse(final Request request, final Response response, final String jsonString) {
    JSONObject obj = JSONParser.parse(jsonString).isObject();
    parse(request, response, obj);
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

  public JSONObject serialize(final Request<?> request) {
    JSONObject obj = new JSONObject();
    obj.put("operation", new JSONString(request.getOperation().getQualifiedName()));

    if (!request.getOperation().getParameters().isEmpty()) {
      JSONArray jsonArgs = new JSONArray();
      int i = 0;
      Object[] args = request.getArgs();
      for (Parameter param : request.getOperation().getParameters().values()) {
        Object arg = args[i];
        JSONValue jsonValue = provider.create(param.getType(), arg, false);
        jsonArgs.set(i, jsonValue);
        if (param.getType() instanceof ObjectType) {
          // TODO many=true
          if (arg instanceof WebEntity) {
            WebEntity entity = (WebEntity) arg;
            message.get().getEntityId(entity);
          }
        }
        i++;
      }
      obj.put("parameters", jsonArgs);
    }

    if (!message.get().getEntities().isEmpty()) {
      JSONArray entities = new JSONArray();
      for (EntityId eid : message.get().getEntityIds()) {
        WebObject entity = message.get().getEntity(eid);
        JSONValue jsonValue = provider.create(entity.getObjectType(), entity, true);
        entities.set(entities.size(), jsonValue);
      }
      obj.put("entities", entities);
    }
    return obj;
  }

  protected void configureRequestBuilder(final RequestBuilder builder) {
    builder.setHeader("Content-Type", RequestFactory.JSON_CONTENT_TYPE_UTF8);
    builder.setHeader("pageurl", Location.getHref());
    builder.setHeader(STRONG_NAME_HEADER, GWT.getPermutationStrongName());
  }

  private void restCall() {
    activeRequest = requests.pop();
    response = responseProvider.get();

    JSONObject obj = serialize(activeRequest);
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
