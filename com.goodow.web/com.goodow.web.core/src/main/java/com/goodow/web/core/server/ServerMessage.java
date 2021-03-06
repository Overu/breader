package com.goodow.web.core.server;

import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.Operation;
import com.goodow.web.core.shared.SerializationException;
import com.goodow.web.core.shared.WebContent;
import com.goodow.web.core.shared.WebContentService;
import com.goodow.web.core.shared.WebService;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class ServerMessage extends Message {

  @Inject
  protected Injector injector;

  @Inject
  JSONMarshaller provider;

  @Override
  public WebContent find(final ObjectType objectType, final String id) {
    WebService<?> service = objectType.getService();
    if (service == null) {
      service = injector.getInstance(objectType.getServiceClass());
      objectType.setService(service);
    }
    return ((WebContentService<?>) service).getById(id);
  }

  public String process(final String payload) throws Exception {
    try {
      provider.parse(request, payload);
      Operation operation = request.getOperation();
      ObjectType targetType = request.getTargetType();
      Class<? extends WebService> serviceClass = targetType.getServiceClass();
      WebService service = injector.getInstance(serviceClass);
      Object result = service.invoke(operation, request.getArgs());
      response.setResult(result);
      String body = provider.serialize(response);
      return body;
    } catch (ReportableException e) {
      e.printStackTrace();
      // response.setGeneralFailure(createFailureMessage(e));
    } catch (SerializationException e) {
      e.printStackTrace();
    }
    return null;
  }

}