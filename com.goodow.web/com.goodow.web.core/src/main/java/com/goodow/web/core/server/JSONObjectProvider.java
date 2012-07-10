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
import com.goodow.web.core.shared.Property;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

@Singleton
public class JSONObjectProvider {

  @Inject
  JSONCollectionProvider collectionProvider;

  public void writeTo(final WebObject source, final JSONObject target, final Message message)
      throws JSONException {
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
          writeTo(obj, json, message);
          target.put(prop.getName(), json);
        }
      } else if (value instanceof Collection) {
        JSONArray array = new JSONArray();
        collectionProvider.writeTo((Collection) value, array, prop.isContainment(), message);
        target.put(prop.getName(), array);
      } else {
        target.put(prop.getName(), value);
      }
    }
  }
}
