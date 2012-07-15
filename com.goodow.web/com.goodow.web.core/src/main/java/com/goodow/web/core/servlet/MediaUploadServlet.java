package com.goodow.web.core.servlet;

import com.goodow.web.core.server.JSONMarshaller;
import com.goodow.web.core.shared.Media;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class MediaUploadServlet extends HttpServlet {

  public static final String END_POINT = "upload";

  @Inject
  JSONMarshaller jsonMarshaller;

  @Override
  protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      ServletMessage message = ServletMessage.create(req, resp).parseMultipartParams(1024 * 1024);
      Media file = message.getFile("attachment");
      String json = jsonMarshaller.serialize(file);
      resp.getWriter().print(json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
