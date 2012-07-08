package com.goodow.web.core.client.json;

import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.Parameter;
import com.goodow.web.core.shared.Property;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.Response;
import com.goodow.web.core.shared.ValueType;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebType;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GwtJSONProvider {

  @Inject
  Message message;

  public void parse(final Request request, final Response response, final JSONObject obj) {
    WebType type = request.getOperation().getType();
    JSONBoolean success = obj.get("success").isBoolean();
    response.setSuccess(success.booleanValue());
    if (success != null && response.isSuccess()) {
      JSONValue jsonResult = obj.get("result");
      Object result = parse(type, jsonResult);
      response.setResult(result);
    } else {
      JSONObject error = obj.get("error").isObject();
    }
  }

  public void parse(final Request request, final Response response, final String jsonString) {
    JSONObject obj = JSONParser.parse(jsonString).isObject();
    parse(request, response, obj);
  }

  public JSONObject serialize(final Request<?> request, final Response<?> response) {
    JSONObject obj = new JSONObject();
    obj.put("operation", new JSONString(request.getOperation().getQualifiedName()));

    if (!request.getOperation().getParameters().isEmpty()) {
      JSONArray jsonArgs = new JSONArray();
      int i = 0;
      Object[] args = request.getArgs();
      for (Parameter param : request.getOperation().getParameters().values()) {
        Object arg = args[i];
        JSONValue jsonValue = create(param.getType(), arg, false);
        jsonArgs.set(i, jsonValue);
        if (param.getType() instanceof ObjectType) {
          // TODO many=true
          if (arg instanceof WebEntity) {
            WebEntity entity = (WebEntity) arg;
            message.getEntityId(entity);
          }
        }
        i++;
      }
      obj.put("parameters", jsonArgs);
    }

    if (!message.getEntities().isEmpty()) {
      JSONArray entities = new JSONArray();
      for (EntityId eid : message.getEntityIds()) {
        WebObject entity = message.getEntity(eid);
        JSONValue jsonValue = create(entity.getObjectType(), entity, true);
        entities.set(entities.size(), jsonValue);
      }
      obj.put("entities", entities);
    }
    return obj;
  }

  private JSONValue create(final WebType type, final Object obj, final boolean convert) {
    if (obj == null) {
      return JSONNull.getInstance();
    } else if (type instanceof ValueType) {
      Class<?> dc = type.getDefinitionClass();
      if (boolean.class.equals(dc) || Boolean.class.equals(dc)) {
        return JSONBoolean.getInstance((Boolean) obj);
      } else if (int.class.equals(dc) || Integer.class.equals(dc)) {
        return new JSONNumber((Integer) obj);
      } else if (long.class.equals(dc) || Long.class.equals(dc)) {
        return new JSONNumber((Long) obj);
      } else {
        return new JSONString((String) obj);
      }
    } else {
      WebObject entity = (WebObject) obj;

      if (convert) {
        JSONObject jsonObject = new JSONObject();
        if (entity instanceof WebEntity) {
          EntityId eid = message.getEntityId((WebEntity) entity);
          jsonObject.put("e_id", new JSONString(eid.toString()));
        }
        for (Property prop : entity.getObjectType().getAllProperties().values()) {
          Object value = entity.get(prop);
          JSONValue jsonValue = create(prop.getType(), value, false);
          jsonObject.put(prop.getName(), jsonValue);
        }
        return jsonObject;
      } else {
        return new JSONString(message.getEntityId((WebEntity) entity).toString());
      }
    }
  }

  private Object parse(final WebType type, final JSONValue json) {
    if (json == null || json.isNull() != null) {
      return null;
    } else if (type instanceof ValueType) {
      Class<?> dc = type.getDefinitionClass();
      if (boolean.class.equals(dc) || Boolean.class.equals(dc)) {
        return json.isBoolean().booleanValue();
      } else if (int.class.equals(dc) || Integer.class.equals(dc)) {
        return (int) json.isNumber().doubleValue();
      } else if (long.class.equals(dc) || Long.class.equals(dc)) {
        return (long) json.isNumber().doubleValue();
      } else {
        return json.isString().stringValue();
      }
    } else {
      ObjectType entityType = (ObjectType) type;
      WebObject obj;
      JSONString jsonString = json.isString();
      if (jsonString != null) {
        String key = json.isString().stringValue();
        EntityId id = EntityId.parseId(key);
        WebEntity entity = message.getEntity(id);
        entity.setId(id.getStableId());
        obj = entity;
      } else {
        JSONObject jsonObj = json.isObject();
        JSONString eId = jsonObj.get("e_id").isString();
        EntityId id = EntityId.parseId(eId.stringValue());
        obj = message.getEntity(id);
        for (Property prop : obj.getObjectType().getAllProperties().values()) {
          JSONValue jsonValue = jsonObj.get(prop.getName());
          Object value = parse(prop.getType(), jsonValue);
          obj.set(prop, value);
        }
      }
      return obj;
    }
  }
}
