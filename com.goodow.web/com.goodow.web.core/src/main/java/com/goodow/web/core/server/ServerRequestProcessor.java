/*
 * Copyright 2010 Google Inc.
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

import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.Operation;
import com.goodow.web.core.shared.Parameter;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.RequestProcessor;
import com.goodow.web.core.shared.Response;
import com.goodow.web.core.shared.WebPlatform;
import com.goodow.web.core.shared.WebService;
import com.goodow.web.core.shared.WebType;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Singleton
public class ServerRequestProcessor extends RequestProcessor {
  @Inject
  WebPlatform platform;
  @Inject
  JsonProvider provider;

  @Inject
  Injector injector;

  public Request parse(final String payload) {
    Request request = requestProvider.get();
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
          provider.parseEntity(jsonObj);
        }
      }
      Object[] args = new Object[operation.getParameters().size()];
      int i = 0;
      for (Parameter param : operation.getParameters().values()) {
        JSONArray values = json.getJSONArray("parameters");
        WebType type = param.getType();
        Object obj = values.get(i);
        args[i] = provider.parse(type, obj);
        i++;
      }
      request.setArgs(args);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return request;
  }

  public String process(final String payload) {
    try {
      Response response = responseProvider.get();
      Request req = parse(payload);
      Operation operation = req.getOperation();
      ObjectType entityType = operation.getDeclaringType();
      Class<? extends WebService> serviceClass = entityType.getServiceClass();
      WebService service = injector.getInstance(serviceClass);
      Object result = service.invoke(operation, req.getArgs());
      response.setResult(result);
      String body = serialize(req, response);
      return body;
    } catch (ReportableException e) {
      e.printStackTrace();
      // response.setGeneralFailure(createFailureMessage(e));
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Response send() {
    // TODO Auto-generated method stub
    return null;
  }

  // private FailureMessage createFailureMessage(final ReportableException e) {
  // ServerFailure failure =
  // exceptionHandler.createServerFailure(e.getCause() == null ? e : e.getCause());
  // FailureMessage msg = null;
  // msg.setExceptionType(failure.getExceptionType());
  // msg.setMessage(failure.getMessage());
  // msg.setStackTrace(failure.getStackTraceString());
  // msg.setFatal(failure.isFatal());
  // return msg;
  // }
  public String serialize(final Request<?> request, final Response<?> response)
      throws JSONException {
    JSONObject jsonObject = new JSONObject();
    Object obj = response.getResult();
    WebType type = request.getOperation().getType();
    provider.serialize(jsonObject, "result", obj, type);
    jsonObject.put("success", true);
    return jsonObject.toString();
  }
}
