package com.goodow.web.core.client;

import static com.google.gwt.user.client.rpc.RpcRequestBuilder.STRONG_NAME_HEADER;

import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.Parameter;
import com.goodow.web.core.shared.Property;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.ValueType;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window.Location;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import java.util.logging.Level;

public class ClientMessage extends Message implements RequestCallback {

  @Override
  public WebEntity find(final ObjectType objectType, final String id) {
    return null;
  }

  @Override
  public Message fire() {
    JSONObject obj = serialize(request);
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
    return this;
  }

  @Override
  public void onError(final com.google.gwt.http.client.Request request, final Throwable exception) {
    logger.finest("onError");
  }

  @Override
  public void onResponseReceived(final com.google.gwt.http.client.Request request,
      final com.google.gwt.http.client.Response response) {
    String body = response.getText();
    JSONValue obj = JSONParser.parse(body);
    WebType type = this.request.getOperation().getType();
    Object result = readFrom(type, obj);
    this.response.setResult(result);
    Receiver r = this.request.getReceiver();
    if (r != null) {
      r.onSuccess(this.response.getResult());
    }
    logger.finest("onResponseReceived");
  }

  public Object readFrom(final WebType type, final JSONValue json) {
    if (json == null || json.isNull() != null) {
      return null;
    }

    if (type instanceof ValueType) {
      Class<?> cls = type.getJavaClass();
      if (boolean.class.equals(cls) || Boolean.class.equals(cls)) {
        return json.isBoolean().booleanValue();
      } else if (int.class.equals(cls) || Integer.class.equals(cls)) {
        return (int) json.isNumber().doubleValue();
      } else if (long.class.equals(cls) || Long.class.equals(cls)) {
        return (long) json.isNumber().doubleValue();
      } else {
        return json.isString().stringValue();
      }
    } else {
      WebObject obj;
      JSONString jsonString = json.isString();
      if (jsonString != null) {
        String key = json.isString().stringValue();
        EntityId id = EntityId.parseId(key);
        WebEntity entity = getEntity(id);
        entity.setId(id.getStableId());
        obj = entity;
      } else {
        JSONObject jsonObj = json.isObject();
        JSONString eId = jsonObj.get("e_id").isString();
        EntityId id = EntityId.parseId(eId.stringValue());
        obj = getEntity(id);
        for (Property prop : obj.getObjectType().getAllProperties().values()) {
          JSONValue jsonValue = jsonObj.get(prop.getName());
          Object value = readFrom(prop.getType(), jsonValue);
          obj.set(prop, value);
        }
      }
      return obj;
    }
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
        JSONValue jsonValue = writeTo(param.getType(), arg);
        jsonArgs.set(i, jsonValue);
        if (param.getType() instanceof ObjectType) {
          // TODO many=true
          if (arg instanceof WebEntity) {
            WebEntity entity = (WebEntity) arg;
            getEntityId(entity);
          }
        }
        i++;
      }
      obj.put("parameters", jsonArgs);
    }

    if (!getEntities().isEmpty()) {
      JSONArray entities = new JSONArray();
      for (EntityId eid : getEntityIds()) {
        WebObject entity = getEntity(eid);
        JSONObject jsonObject = writeTo(entity);
        entities.set(entities.size(), jsonObject);
      }
      obj.put("entities", entities);
    }
    return obj;
  }

  public JSONObject writeTo(final WebObject obj) {
    JSONObject jsonObject = new JSONObject();
    if (obj instanceof WebEntity) {
      EntityId eid = getEntityId((WebEntity) obj);
      jsonObject.put("e_id", new JSONString(eid.toString()));
    }
    for (Property prop : obj.getObjectType().getAllProperties().values()) {
      Object value = obj.get(prop);
      JSONValue jsonValue = writeTo(prop.getType(), value);
      jsonObject.put(prop.getName(), jsonValue);
    }
    return jsonObject;
  }

  public JSONValue writeTo(final WebType type, final Object obj) {
    if (obj == null) {
      return JSONNull.getInstance();
    }

    if (obj instanceof WebEntity) {
      EntityId id = getEntityId((WebEntity) obj);
      return new JSONString(id.toString());
    } else if (obj instanceof WebObject) {
      return writeTo((WebObject) obj);
    } else {
      Class<?> dc = type.getJavaClass();
      if (boolean.class.equals(dc) || Boolean.class.equals(dc)) {
        return JSONBoolean.getInstance((Boolean) obj);
      } else if (int.class.equals(dc) || Integer.class.equals(dc)) {
        return new JSONNumber((Integer) obj);
      } else if (long.class.equals(dc) || Long.class.equals(dc)) {
        return new JSONNumber((Long) obj);
      } else {
        return new JSONString((String) obj);
      }
    }
  }

  protected void configureRequestBuilder(final RequestBuilder builder) {
    builder.setHeader("Content-Type", RequestFactory.JSON_CONTENT_TYPE_UTF8);
    builder.setHeader("pageurl", Location.getHref());
    builder.setHeader(STRONG_NAME_HEADER, GWT.getPermutationStrongName());
  }

}
