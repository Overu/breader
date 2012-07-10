package com.goodow.web.core.server;

import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.Operation;
import com.goodow.web.core.shared.Parameter;
import com.goodow.web.core.shared.Property;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.Response;
import com.goodow.web.core.shared.ValueType;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebPlatform;
import com.goodow.web.core.shared.WebService;
import com.goodow.web.core.shared.WebType;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

@Singleton
public class ServerMessage extends Message {

  @Inject
  protected WebPlatform platform;

  @Inject
  protected Injector injector;

  @Inject
  JSONObjectProvider objectProvider;

  @Inject
  JSONCollectionProvider collectionProvider;

  @Override
  public WebEntity find(final ObjectType objectType, final String id) {
    WebService<?> service = objectType.getService();
    if (service == null) {
      service = injector.getInstance(objectType.getServiceClass());
      objectType.setService(service);
    }
    return service.find(id);
  }

  public Request parse(final String payload) {
    Request request = getRequest();
    JSONObject json;
    try {
      json = new JSONObject(payload);
      String operationName = json.getString("operation");
      Operation operation = platform.getOperation(operationName);
      request.setOperation(operation);

      JSONArray entities = json.getJSONArray("entities");

      if (entities != null) {
        for (int i = 0; i < entities.length(); i++) {
          JSONObject jsonObj = entities.getJSONObject(i);
          parseEntity(jsonObj);
        }
      }
      Object[] args = new Object[operation.getParameters().size()];
      int i = 0;
      for (Parameter param : operation.getParameters().values()) {
        JSONArray values = json.getJSONArray("parameters");
        WebType type = param.getType();
        Object obj = values.get(i);
        args[i] = parse(type, obj);
        i++;
      }
      request.setArgs(args);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return request;
  }

  public Object parse(final WebType type, final Object obj) throws JSONException {
    Class<?> dc = type.getJavaClass();
    if (obj == null || obj.equals(JSONObject.NULL)) {
      return null;
    } else if (obj instanceof JSONObject) {
      JSONObject jsonObject = (JSONObject) obj;
      return parseEntity(jsonObject);
    } else if (type instanceof ValueType) {
      if (boolean.class.equals(dc) || Boolean.class.equals(dc)) {
        return (Boolean) obj;
      } else if (int.class.equals(dc) || Integer.class.equals(dc)) {
        return ((Number) obj).intValue();
      } else if (long.class.equals(dc) || Long.class.equals(dc)) {
        return ((Number) obj).longValue();
      } else {
        return (String) obj;
      }
    } else {
      String eId = (String) obj;
      EntityId id = EntityId.parseId(eId);
      return getEntity(id);
    }
  }

  public WebEntity parseEntity(final JSONObject jsonObject) throws JSONException {
    if (jsonObject == null) {
      return null;
    } else {
      String eId = jsonObject.getString("e_id");
      EntityId id = EntityId.parseId(eId);
      WebEntity entity = getEntity(id);
      for (Property prop : entity.getObjectType().getAllProperties().values()) {
        if (jsonObject.has(prop.getName())) {
          Object jsonValue = jsonObject.get(prop.getName());
          Object value = parse(prop.getType(), jsonValue);
          entity.set(prop, value);
        }
      }
      return entity;
    }
  }

  public String process(final String payload) {
    try {
      Response response = getResponse();
      Request req = parse(payload);
      Operation operation = req.getOperation();
      ObjectType entityType = operation.getDeclaringType();
      Class<? extends WebService> serviceClass = entityType.getServiceClass();
      WebService service = injector.getInstance(serviceClass);
      Object result = service.invoke(operation, req.getArgs());
      response.setResult(result);
      String body = serialize();
      return body;
    } catch (ReportableException e) {
      e.printStackTrace();
      // response.setGeneralFailure(createFailureMessage(e));
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  public String serialize() throws JSONException {
    Object result = response.getResult();
    if (result == null) {
      return "null";
    } else if (result instanceof WebObject) {
      WebObject obj = (WebObject) result;
      JSONObject jsonObject = new JSONObject();
      objectProvider.writeTo(obj, jsonObject, this);
      return jsonObject.toString();
    } else if (result instanceof Collection) {
      JSONArray jsonArray = new JSONArray();
      collectionProvider.writeTo((Collection) result, jsonArray, true, this);
      return jsonArray.toString();
    } else if (result instanceof String) {
      return "\"" + result + "\"";
    } else {
      return result.toString();
    }
  }

}
