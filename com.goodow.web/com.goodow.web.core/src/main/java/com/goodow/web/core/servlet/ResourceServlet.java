package com.goodow.web.core.servlet;

import com.goodow.web.core.server.JSONMarshaller;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.ResourceService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class ResourceServlet extends HttpServlet {

  public static final String END_POINT = "resources";

  @Inject
  JSONMarshaller jsonMarshaller;

  @Inject
  ResourceService resourceService;

  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    String uri = req.getRequestURI();
    String id = uri.substring(uri.lastIndexOf("/") + 1);
    Resource resource = resourceService.find(id);
    String contentType = resource.getContentType();
    if (contentType != null) {
      resp.setContentType(contentType);
    }
    ServletMessage.writeResource(resource, resp.getOutputStream());
  }

  @Override
  protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      ServletMessage message =
          ServletMessage.create(req, resp).parseMultipartParams(1024 * 1024 * 1024);
      Resource file = message.getFile("attachment");
      resourceService.save(file);
      String json = jsonMarshaller.serialize(file);
      resp.getWriter().print(json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
