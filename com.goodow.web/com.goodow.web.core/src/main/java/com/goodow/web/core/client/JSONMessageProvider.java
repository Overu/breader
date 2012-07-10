/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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

public class JSONMessageProvider {

  public Object convertFrom(final WebType type, final JSONValue json, final Message message) {
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
        WebEntity entity = message.getEntity(id);
        entity.setId(id.getStableId());
        obj = entity;
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

  public JSONValue convertTo(final WebType type, final Object obj, final Message message) {
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

  public JSONObject serialize(final Message message) {
    JSONObject obj = new JSONObject();
    Request request = message.getRequest();
    obj.put("operation", new JSONString(request.getOperation().getQualifiedName()));

    if (!request.getOperation().getParameters().isEmpty()) {
      JSONArray jsonArgs = new JSONArray();
      int i = 0;
      Object[] args = request.getArgs();
      for (Parameter param : request.getOperation().getParameters().values()) {
        Object arg = args[i];
        JSONValue jsonValue = convertTo(param.getType(), arg, message);
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
}
