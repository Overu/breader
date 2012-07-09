package com.goodow.web.core.server;

import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.Property;
import com.goodow.web.core.shared.ValueType;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebPlatform;
import com.goodow.web.core.shared.WebType;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonProvider {

  @Inject
  Provider<Message> message;

  @Inject
  WebPlatform platform;

  public Object parse(final WebType type, final Object obj) throws JSONException {
    Class<?> dc = type.getDefinitionClass();
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
      Message m = message.get();
      return m.getEntity(id);
    }
  }

  public WebEntity parseEntity(final JSONObject jsonObject) throws JSONException {
    if (jsonObject == null) {
      return null;
    } else {
      String eId = jsonObject.getString("e_id");
      EntityId id = EntityId.parseId(eId);
      Message m = message.get();
      WebEntity entity = m.getEntity(id);
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

  public void serialize(final JSONObject parent, final String key, final Object value,
      final WebType type) throws JSONException {
    Message m = message.get();
    if (value == null) {
      parent.put(key, JSONObject.NULL);
    } else if (type instanceof ValueType) {
      parent.put(key, value);
    } else {
      WebObject entity = (WebObject) value;
      JSONObject jsonObject = new JSONObject();
      if (entity instanceof WebEntity) {
        EntityId eid = m.getEntityId((WebEntity) entity);
        jsonObject.put("e_id", eid.toString());
      }
      for (Property prop : entity.getObjectType().getAllProperties().values()) {
        Object propValue = entity.get(prop);
        WebType propType = prop.getType();
        if (propType instanceof ObjectType) {
          WebObject entityValue = (WebObject) propValue;
          if (entityValue == null) {
            jsonObject.put(prop.getName(), JSONObject.NULL);
          } else if (prop.isContainment()) {
            serialize(jsonObject, prop.getName(), propValue, prop.getType());
          } else if (entityValue instanceof WebEntity) {
            EntityId id = m.getEntityId((WebEntity) entityValue);
            jsonObject.put(prop.getName(), id.toString());
          }
        } else {
          jsonObject.put(prop.getName(), propValue);
        }
      }
      parent.put(key, jsonObject);
    }
  }

}
