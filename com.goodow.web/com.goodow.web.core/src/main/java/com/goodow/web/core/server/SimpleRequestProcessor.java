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

import org.json.JSONException;

import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.Operation;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.Response;
import com.goodow.web.core.shared.WebService;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class SimpleRequestProcessor {

  // @Inject
  // private ExceptionHandler exceptionHandler;

  @Inject
  private Provider<Request> requestProvider;

  @Inject
  private Provider<Response> responseProvider;

  @Inject
  JsonBuilder builder;

  @Inject
  Injector injector;

  public String process(final String payload) {
    com.goodow.web.core.shared.Request req;
    // TODO read request from payload
    Response response = responseProvider.get();
    try {
      req = builder.parse(payload);
      Operation operation = req.getOperation();
      ObjectType entityType = operation.getDeclaringType();
      Class<? extends WebService> serviceClass = entityType.getServiceClass();
      WebService service = injector.getInstance(serviceClass);
      Object result = service.invoke(operation, req.getArgs());
      response.setResult(result);
      String body = builder.serialize(req, response);
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

}
