package com.goodow.web.core.server;

import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectReader;
import com.goodow.web.core.shared.ObjectWriter;
import com.goodow.web.core.shared.Property;
import com.goodow.web.core.shared.SerializationException;
import com.goodow.web.core.shared.WebContent;
import com.goodow.web.core.shared.WebObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

@Singleton
public class JSONObjectProvider<T extends WebObject> implements ObjectReader<T, JSONObject>,
    ObjectWriter<T, JSONObject> {

  @Inject
  JSONMarshaller marshaller;

  @Override
  public void readFrom(final T obj, final JSONObject json, final Message message) {
    try {
      for (Property prop : obj.getObjectType().getAllProperties().values()) {
        if (json.has(prop.getName())) {
          Object jsonValue = json.get(prop.getName());
          Object value = marshaller.parse(prop.getType(), jsonValue, message);
          obj.set(prop, value);
        }
      }
    } catch (JSONException e) {
      throw new SerializationException(e);
    }
  }

  @Override
  public void writeTo(final T source, final JSONObject target, final Message message) {
    try {
      if (source instanceof WebContent) {
        EntityId eid = message.getEntityId((WebContent) source);
        target.put("e_id", eid.toString());
      }
      for (Property prop : source.getObjectType().getAllProperties().values()) {

        Object value = source.get(prop);
        if (prop.getName().equals("container") && value != null) {
          WebContent entity = (WebContent) value;
          EntityId id = message.getEntityId(entity);
          target.put(prop.getName(), id.toString());
        } else {
          Object json = marshaller.convertToJSON(message, value);
          target.put(prop.getName(), json);
        }
      }
    } catch (JSONException e) {
      throw new SerializationException(e);
    }
  }
}
