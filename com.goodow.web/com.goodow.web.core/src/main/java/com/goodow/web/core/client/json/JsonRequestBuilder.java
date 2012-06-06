package com.goodow.web.core.client.json;

import com.goodow.web.core.shared.CorePackage;
import com.goodow.web.core.shared.Entity;
import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.EntityType;
import com.goodow.web.core.shared.Parameter;
import com.goodow.web.core.shared.Property;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.Response;
import com.goodow.web.core.shared.Type;
import com.goodow.web.core.shared.ValueType;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.inject.Singleton;

@Singleton
public class JsonRequestBuilder {

  public void parse(final Request request, final Response response, final JSONObject obj) {
    Type type = request.getOperation().getType();
    JSONBoolean success = obj.get("success").isBoolean();
    response.setSuccess(success.booleanValue());
    if (success != null && response.isSuccess()) {
      JSONValue jsonResult = obj.get("result");
      Object result = parse(request, type, jsonResult);
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
        JSONValue jsonValue = create(request, param.getType(), arg, false);
        jsonArgs.set(i, jsonValue);
        if (param.getType() instanceof EntityType) {
          // TODO many=true
          Entity entity = (Entity) arg;
          request.getEntityId(entity);
        }
        i++;
      }
      obj.put("parameters", jsonArgs);
    }

    if (!request.getEntities().isEmpty()) {
      JSONArray entities = new JSONArray();
      for (EntityId eid : request.getEntityIds()) {
        Entity entity = request.getEntity(eid);
        JSONValue jsonValue = create(request, entity.type(), entity, true);
        entities.set(entities.size(), jsonValue);
      }
      obj.put("entities", entities);
    }
    return obj;
  }

  private JSONValue create(final Request<?> request, final Type type, final Object obj,
      final boolean convert) {
    if (obj == null) {
      return JSONNull.getInstance();
    } else if (type instanceof ValueType) {
      if (type == CorePackage.Boolean.as() || type == CorePackage.BOOLEAN.as()) {
        return JSONBoolean.getInstance((Boolean) obj);
      } else if (type == CorePackage.INT.as() || type == CorePackage.Integer.as()) {
        return new JSONNumber((Integer) obj);
      } else if (type == CorePackage.LONG.as() || type == CorePackage.Long.as()) {
        return new JSONNumber((Long) obj);
      } else {
        return new JSONString((String) obj);
      }
    } else {
      Entity entity = (Entity) obj;
      EntityId eid = request.getEntityId(entity);
      if (convert) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("e_id", new JSONString(eid.toString()));
        for (Property prop : entity.type().getAllProperties().values()) {
          Object value = entity.get(prop);
          JSONValue jsonValue = create(request, prop.getType(), value, false);
          jsonObject.put(prop.getName(), jsonValue);
        }
        return jsonObject;
      } else {
        return new JSONString(eid.toString());
      }
    }
  }

  private Object parse(final Request<?> request, final Type type, final JSONValue json) {
    if (json == null || json.isNull() != null) {
      return null;
    } else if (type instanceof ValueType) {
      if (type == CorePackage.Boolean.as() || type == CorePackage.BOOLEAN.as()) {
        return json.isBoolean().booleanValue();
      } else if (type == CorePackage.INT.as() || type == CorePackage.Integer.as()) {
        return (int) json.isNumber().doubleValue();
      } else if (type == CorePackage.LONG.as() || type == CorePackage.Long.as()) {
        return (long) json.isNumber().doubleValue();
      } else {
        return json.isString().stringValue();
      }
    } else {
      EntityType entityType = (EntityType) type;
      Entity entity;
      JSONString jsonString = json.isString();
      if (jsonString != null) {
        String key = json.isString().stringValue();
        EntityId id = EntityId.parseId(key);
        entity = request.getEntity(id);
        entity.setId(id.getStableId());
      } else {
        JSONObject jsonObj = json.isObject();
        JSONString eId = jsonObj.get("e_id").isString();
        EntityId id = EntityId.parseId(eId.stringValue());
        entity = request.getEntity(id);
        for (Property prop : entity.type().getAllProperties().values()) {
          JSONValue jsonValue = jsonObj.get(prop.getName());
          Object value = parse(request, prop.getType(), jsonValue);
          entity.set(prop, value);
        }
      }
      return entity;
    }
  }
}
