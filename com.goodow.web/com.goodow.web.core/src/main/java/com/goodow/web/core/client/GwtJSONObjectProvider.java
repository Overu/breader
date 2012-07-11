package com.goodow.web.core.client;

import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectReader;
import com.goodow.web.core.shared.ObjectWriter;
import com.goodow.web.core.shared.Property;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebObject;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GwtJSONObjectProvider<T extends WebObject> implements ObjectReader<T, JSONObject>,
    ObjectWriter<T, JSONObject> {

  @Inject
  ClientJSONMarshaller marshaller;

  @Override
  public void readFrom(final T obj, final JSONObject jsonObj, final Message message) {
    for (Property prop : obj.getObjectType().getAllProperties().values()) {
      JSONValue jsonValue = jsonObj.get(prop.getName());
      Object value = marshaller.parse(prop.getType(), jsonValue, message);
      obj.set(prop, value);
    }
  }

  @Override
  public void writeTo(final T obj, final JSONObject json, final Message message) {
    if (obj instanceof WebEntity) {
      EntityId eid = message.getEntityId((WebEntity) obj);
      json.put("e_id", new JSONString(eid.toString()));
    }
    for (Property prop : obj.getObjectType().getAllProperties().values()) {
      Object value = obj.get(prop);
      JSONValue jsonValue = marshaller.serialize(prop.getType(), value, message);
      json.put(prop.getName(), jsonValue);
    }
  }
}
