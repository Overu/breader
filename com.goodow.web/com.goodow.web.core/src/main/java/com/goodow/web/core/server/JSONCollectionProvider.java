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
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebObject;

import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

public class JSONCollectionProvider {

  @Inject
  JSONObjectProvider provider;

  public void writeTo(final Collection collection, final JSONArray target,
      final boolean containment, final Message message) throws JSONException {
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
