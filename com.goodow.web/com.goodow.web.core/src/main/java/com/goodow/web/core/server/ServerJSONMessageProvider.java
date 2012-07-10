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
package com.goodow.web.core.server;

import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectProvider;
import com.goodow.web.core.shared.Operation;
import com.goodow.web.core.shared.Parameter;
import com.goodow.web.core.shared.Property;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.Response;
import com.goodow.web.core.shared.ValueType;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebPlatform;
import com.goodow.web.core.shared.WebType;

import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

public class ServerJSONMessageProvider {

  @Inject
  protected WebPlatform platform;

  public void parse(final Request request, final String payload) {
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
          parseEntity(jsonObj, request.getMessage());
        }
      }
      Object[] args = new Object[operation.getParameters().size()];
      int i = 0;
      for (Parameter param : operation.getParameters().values()) {
        JSONArray values = json.getJSONArray("parameters");
        WebType type = param.getType();
        Object obj = values.get(i);
        args[i] = parse(type, obj, request.getMessage());
        i++;
      }
      request.setArgs(args);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public Object parse(final WebType type, final Object obj, final Message message)
      throws JSONException {
    Class<?> dc = type.getJavaClass();
    if (obj == null || obj.equals(JSONObject.NULL)) {
      return null;
    } else if (obj instanceof JSONObject) {
      JSONObject jsonObject = (JSONObject) obj;
      return parseEntity(jsonObject, message);
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
      return message.getEntity(id);
    }
  }

  public WebEntity parseEntity(final JSONObject jsonObject, final Message message)
      throws JSONException {
    if (jsonObject == null) {
      return null;
    } else {
      String eId = jsonObject.getString("e_id");
      EntityId id = EntityId.parseId(eId);
      WebEntity entity = message.getEntity(id);
      for (Property prop : entity.getObjectType().getAllProperties().values()) {
        if (jsonObject.has(prop.getName())) {
          Object jsonValue = jsonObject.get(prop.getName());
          Object value = parse(prop.getType(), jsonValue, message);
          entity.set(prop, value);
        }
      }
      return entity;
    }
  }

  public String serialize(final Response response) throws Exception {
    Object result = response.getResult();
    if (result == null) {
      return "null";
    } else if (result instanceof WebObject) {
      WebObject obj = (WebObject) result;
      JSONObject jsonObject = new JSONObject();
      ObjectProvider<WebObject, JSONObject> provider =
          obj.getObjectType().getProvider(JSONObject.class);
      provider.writeTo(obj, jsonObject, response.getMessage());
      return jsonObject.toString();
    } else if (result instanceof Collection) {
      JSONArray jsonArray = new JSONArray();
      writeTo((Collection) result, jsonArray, true, response.getMessage());
      return jsonArray.toString();
    } else if (result instanceof String) {
      return "\"" + result + "\"";
    } else {
      return result.toString();
    }
  }

  public void writeTo(final Collection collection, final JSONArray target,
      final boolean containment, final Message message) throws Exception {
    for (Object value : collection) {
      if (value == null) {
        target.put(JSONObject.NULL);
      } else if (value instanceof WebObject) {
        WebObject obj = (WebObject) value;
        if (obj instanceof WebEntity && !containment) {
          EntityId id = message.getEntityId((WebEntity) obj);
          target.put(id.toString());
        } else {
          JSONObject json = new JSONObject();
          ObjectProvider<WebObject, JSONObject> provider =
              obj.getObjectType().getProvider(JSONObject.class);
          provider.writeTo(obj, json, message);
          target.put(json);
        }
      } else if (value instanceof Collection) {
        JSONArray array = new JSONArray();
        writeTo((Collection) value, array, containment, message);
        target.put(array);
      } else {
        target.put(value);
      }
    }
  }
}
