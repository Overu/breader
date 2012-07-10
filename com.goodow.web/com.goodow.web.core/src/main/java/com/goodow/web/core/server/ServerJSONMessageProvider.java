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
import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.Operation;
import com.goodow.web.core.shared.Parameter;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.Response;
import com.goodow.web.core.shared.ValueType;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebPlatform;
import com.goodow.web.core.shared.WebType;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

public class ServerJSONMessageProvider {

  @Inject
  protected WebPlatform platform;

  @Inject
  protected Provider<Message> messageProvider;

  public WebObject parse(final JSONObject json, final Message message) throws Exception {
    if (json == null) {
      return null;
    } else {
      WebObject object = null;
      String eId = json.getString("e_id");
      if (eId != null) {
        EntityId id = EntityId.parseId(eId);
        object = message.getEntity(id);
        ObjectProvider<WebObject, JSONObject> provider =
            id.getObjectType().getProvider(JSONObject.class);
        provider.readFrom(object, json, message);
      }
      return object;
    }
  }

  public WebObject parse(final ObjectType objectType, final JSONObject json, final Message message)
      throws Exception {
    if (json == null) {
      return null;
    } else {
      WebObject object;
      String eId = json.getString("e_id");
      if (eId != null) {
        EntityId id = EntityId.parseId(eId);
        object = message.getEntity(id);
      } else {
        object = objectType.create();
      }
      ObjectProvider<WebObject, JSONObject> provider = objectType.getProvider(JSONObject.class);
      provider.readFrom(object, json, message);
      return object;
    }
  }

  public void parse(final Request request, final String payload) throws Exception {
    try {
      JSONObject json = new JSONObject(payload);
      String operationName = json.getString("operation");
      Operation operation = platform.getOperation(operationName);
      request.setOperation(operation);
      JSONArray entities = json.getJSONArray("entities");
      if (entities != null) {
        for (int i = 0; i < entities.length(); i++) {
          JSONObject jsonObj = entities.getJSONObject(i);
          parse(jsonObj, request.getMessage());
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
      e.printStackTrace();
    }
  }

  public Object parse(final WebType type, final Object obj, final Message message) throws Exception {
    Class<?> dc = type.getJavaClass();
    if (obj == null || obj.equals(JSONObject.NULL)) {
      return null;
    } else if (type instanceof ObjectType) {
      if (obj instanceof String) {
        String eid = (String) obj;
        EntityId id = EntityId.parseId(eid);
        return message.getEntity(id);
      } else {
        JSONObject json = (JSONObject) obj;
        ObjectType objType = (ObjectType) type;
        return parse(objType, json, message);
      }
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

  public JSONArray serialize(final Collection<?> collection, final boolean containment,
      final Message message) throws Exception {
    JSONArray array = new JSONArray();
    for (Object item : collection) {
      if (item == null) {
        array.put(JSONObject.NULL);
      } else if (item instanceof WebObject) {
        WebObject obj = (WebObject) item;
        if (obj instanceof WebEntity && !containment) {
          EntityId id = message.getEntityId((WebEntity) obj);
          array.put(id.toString());
        } else {
          JSONObject json = new JSONObject();
          ObjectProvider<WebObject, JSONObject> provider =
              obj.getObjectType().getProvider(JSONObject.class);
          provider.writeTo(obj, json, message);
          array.put(json);
        }
      } else if (item instanceof Collection) {
        JSONArray value = serialize((Collection<?>) item, containment, message);
        array.put(value);
      } else {
        array.put(item);
      }
    }
    return array;
  }

  public String serialize(final Object obj) throws Exception {
    if (obj instanceof Response) {
      return serialize((Response<?>) obj);
    } else if (obj instanceof Request) {
      return serialize((Request<?>) obj);
    } else {
      return serialize(obj, messageProvider.get());
    }
  }

  public String serialize(final Object result, final Message message) throws Exception {
    if (result == null) {
      return "null";
    } else if (result instanceof WebObject) {
      WebObject obj = (WebObject) result;
      JSONObject jsonObject = new JSONObject();
      ObjectProvider<WebObject, JSONObject> provider =
          obj.getObjectType().getProvider(JSONObject.class);
      provider.writeTo(obj, jsonObject, message);
      return jsonObject.toString();
    } else if (result instanceof Collection) {
      JSONArray array = serialize((Collection<?>) result, true, message);
      return array.toString();
    } else if (result instanceof String) {
      return "\"" + result + "\"";
    } else {
      return result.toString();
    }
  }

  public String serialize(final Request<?> request) throws Exception {
    return request.toString();
  }

  public String serialize(final Response<?> response) throws Exception {
    Object result = response.getResult();
    return serialize(result, response.getMessage());
  }
}
