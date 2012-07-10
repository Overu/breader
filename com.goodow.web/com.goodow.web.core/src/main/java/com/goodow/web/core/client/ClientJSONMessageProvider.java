package com.goodow.web.core.client;

import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectProvider;
import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.Parameter;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.ValueType;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebType;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class ClientJSONMessageProvider {

  public Object parse(final WebType type, final JSONValue json, final Message message)
      throws Exception {
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
      JSONObject jsonObj = json.isObject();
      if (jsonString != null) {
        String key = json.isString().stringValue();
        EntityId id = EntityId.parseId(key);
        obj = message.getEntity(id);
        if (obj instanceof WebEntity) {
          ((WebEntity) obj).setId(id.getStableId());
        }
      } else {
        JSONString eId = jsonObj.get("e_id").isString();
        EntityId id = EntityId.parseId(eId.stringValue());
        obj = message.getEntity(id);
        ObjectProvider<WebObject, JSONObject> provider =
            obj.getObjectType().getProvider(JSONObject.class);
        provider.readFrom(obj, jsonObj, message);

      }
      return obj;
    }
  }

  public JSONObject serialize(final Message message) throws Exception {
    JSONObject obj = new JSONObject();
    Request request = message.getRequest();
    obj.put("operation", new JSONString(request.getOperation().getQualifiedName()));

    if (!request.getOperation().getParameters().isEmpty()) {
      JSONArray jsonArgs = new JSONArray();
      int i = 0;
      Object[] args = request.getArgs();
      for (Parameter param : request.getOperation().getParameters().values()) {
        Object arg = args[i];
        JSONValue jsonValue = serialize(param.getType(), arg, message);
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
        JSONObject json = new JSONObject();
        ObjectProvider<WebObject, JSONObject> provider =
            entity.getObjectType().getProvider(JSONObject.class);
        provider.writeTo(entity, json, message);
        entities.set(entities.size(), json);
      }
      obj.put("entities", entities);
    }
    return obj;
  }

  public JSONValue serialize(final WebType type, final Object obj, final Message message)
      throws Exception {
    if (obj == null) {
      return JSONNull.getInstance();
    }

    if (obj instanceof WebEntity) {
      EntityId id = message.getEntityId((WebEntity) obj);
      return new JSONString(id.toString());
    } else if (obj instanceof WebObject) {
      WebObject o = (WebObject) obj;
      JSONObject json = new JSONObject();
      ObjectProvider<WebObject, JSONObject> provider =
          o.getObjectType().getProvider(JSONObject.class);
      provider.writeTo(o, json, message);
      return json;
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
}
