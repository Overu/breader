package com.goodow.web.core.server;

import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectReader;
import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.ObjectWriter;
import com.goodow.web.core.shared.Operation;
import com.goodow.web.core.shared.Parameter;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.Response;
import com.goodow.web.core.shared.SerializationException;
import com.goodow.web.core.shared.ValueType;
import com.goodow.web.core.shared.WebContent;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebPlatform;
import com.goodow.web.core.shared.WebType;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Date;

public class JSONMarshaller {

  @Inject
  protected Provider<Message> messageProvider;

  public Object convertToJSON(final Message message, final Object value) {
    if (value == null) {
      return JSONObject.NULL;
    } else if (value instanceof WebObject) {
      WebObject obj = (WebObject) value;
      if (obj instanceof WebContent) {
        WebContent entity = (WebContent) obj;
        EntityId id = message.getEntityId(entity);
        if (message.isSerialized(entity)) {
          return id.toString();
        } else {
          message.setSerialized(entity);
        }
      }
      JSONObject json = new JSONObject();
      ObjectWriter<WebObject, JSONObject> writer = obj.getObjectType().getWriter(JSONObject.class);
      writer.writeTo(obj, json, message);
      return json;
    } else if (value instanceof Collection) {
      JSONArray array = new JSONArray();
      for (Object item : (Collection<?>) value) {
        Object itemValue = convertToJSON(message, item);
        array.put(itemValue);
      }
      return array;
    } else if (value.getClass().isArray()) {
      JSONArray array = new JSONArray();
      for (Object item : (Object[]) value) {
        Object itemValue = convertToJSON(message, item);
        array.put(itemValue);
      }
      return array;
    } else if (value instanceof Date) {
      return ((Date) value).getTime();
    } else {
      return value;
    }
  }

  public WebObject parse(final JSONObject json, final Message message) {
    if (json == null) {
      return null;
    } else {
      try {
        WebObject object = null;
        String eId = json.getString("e_id");
        if (eId != null) {
          EntityId id = EntityId.parseId(eId);
          object = message.getEntity(id);
          ObjectReader<WebObject, JSONObject> provider =
              id.getObjectType().getProvider(JSONObject.class);
          provider.readFrom(object, json, message);
        }
        return object;
      } catch (JSONException e) {
        throw new SerializationException(e);
      }
    }
  }

  public WebObject parse(final ObjectType objectType, final JSONObject json, final Message message) {
    if (json == null) {
      return null;
    } else {
      try {
        WebObject object;
        String eId = json.getString("e_id");
        if (eId != null) {
          EntityId id = EntityId.parseId(eId);
          object = message.getEntity(id);
        } else {
          object = objectType.create();
        }
        ObjectReader<WebObject, JSONObject> provider = objectType.getProvider(JSONObject.class);
        provider.readFrom(object, json, message);
        return object;
      } catch (JSONException e) {
        throw new SerializationException(e);
      }
    }
  }

  public void parse(final Request request, final String payload) {
    try {
      JSONObject json = new JSONObject(payload);
      String operationName = json.getString("operation");
      int index = operationName.lastIndexOf(".");
      String typeName = operationName.substring(0, index);
      ObjectType targetType = WebPlatform.getInstance().getObjectType(typeName);
      request.setTargetType(targetType);
      String operationSimpleName = operationName.substring(index + 1);
      Operation operation = targetType.getOperation(operationSimpleName);
      request.setOperation(operation);
      if (json.has("entities")) {
        JSONArray entities = json.getJSONArray("entities");
        if (entities != null) {
          for (int i = 0; i < entities.length(); i++) {
            JSONObject jsonObj = entities.getJSONObject(i);
            parse(jsonObj, request.getMessage());
          }
        }
      }
      Object[] args = new Object[operation.getParameters().size()];
      if (json.has("parameters")) {
        JSONArray values = json.getJSONArray("parameters");
        int i = 0;
        for (Parameter param : operation.getParameters().values()) {

          WebType type = param.getType();
          Object obj = values.get(i);
          args[i] = parse(type, obj, request.getMessage());
          i++;
        }
      }
      request.setArgs(args);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public Object parse(final WebType type, final Object obj, final Message message) {
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
      } else if (Date.class.equals(dc)) {
        return new Date((Long) obj);
      } else {
        return (String) obj;
      }
    } else {
      String eId = (String) obj;
      EntityId id = EntityId.parseId(eId);
      return message.getEntity(id);
    }
  }

  public String serialize(final Object obj) {
    if (obj instanceof Response) {
      return serialize((Response<?>) obj);
    } else if (obj instanceof Request) {
      return serialize((Request<?>) obj);
    } else {
      return convertToJSON(messageProvider.get(), obj).toString();
    }
  }

  public String serialize(final Response<?> response) {
    Object result = response.getResult();
    return convertToJSON(response.getMessage(), result).toString();
  }
}
