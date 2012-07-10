package com.goodow.web.core.server;

import com.goodow.web.core.shared.EntityId;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectProvider;
import com.goodow.web.core.shared.Property;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;

@Singleton
public class JSONObjectProvider<T extends WebObject> implements ObjectProvider<T, JSONObject> {

  @Inject
  ServerJSONMessageProvider messageProvider;

  @Override
  public void readFrom(final T obj, final JSONObject source, final Message message)
      throws Exception {
    // TODO Auto-generated method stub
  }

  @Override
  public void writeTo(final T source, final JSONObject target, final Message message)
      throws Exception {
    if (source instanceof WebEntity) {
      EntityId eid = message.getEntityId((WebEntity) source);
      target.put("e_id", eid.toString());
    }
    for (Property prop : source.getObjectType().getAllProperties().values()) {
      Object value = source.get(prop);
      if (value == null) {
        target.put(prop.getName(), JSONObject.NULL);
      } else if (value instanceof WebObject) {
        WebObject obj = (WebObject) value;
        if (obj instanceof WebEntity && !prop.isContainment()) {
          EntityId id = message.getEntityId((WebEntity) obj);
          target.put(prop.getName(), id.toString());
        } else {
          JSONObject json = new JSONObject();
          ObjectProvider<WebObject, JSONObject> provider =
              obj.getObjectType().getProvider(JSONObject.class);
          provider.writeTo(obj, json, message);
          target.put(prop.getName(), json);
        }
      } else if (value instanceof Collection) {
        JSONArray array = new JSONArray();
        messageProvider.writeTo((Collection) value, array, prop.isContainment(), message);
        target.put(prop.getName(), array);
      } else {
        target.put(prop.getName(), value);
      }
    }
  }
}
