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
package com.goodow.web.core.servlet;

import com.goodow.web.core.server.SimpleRequestProcessor;
import com.goodow.web.core.shared.RequestManager;

import com.google.gwt.user.server.rpc.RPCServletUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles GWT RequestFactory JSON requests.
 */
@SuppressWarnings("serial")
@Singleton
public class WebServiceServlet extends HttpServlet {

  public static final String END_POINT = "rpc";

  private static final boolean DUMP_PAYLOAD = Boolean.getBoolean("gwt.rpc.dumpPayload");
  private static final String JSON_CHARSET = "UTF-8";
  private static final String JSON_CONTENT_TYPE = "application/json";
  private static final Logger log = Logger.getLogger(WebServiceServlet.class.getCanonicalName());

  /**
   * These ThreadLocals are used to allow service objects to obtain access to the HTTP transaction.
   */
  private static final ThreadLocal<ServletContext> perThreadContext =
      new ThreadLocal<ServletContext>();
  private static final ThreadLocal<HttpServletRequest> perThreadRequest =
      new ThreadLocal<HttpServletRequest>();
  private static final ThreadLocal<HttpServletResponse> perThreadResponse =
      new ThreadLocal<HttpServletResponse>();

  /**
   * Returns the thread-local {@link HttpServletRequest}.
   * 
   * @return an {@link HttpServletRequest} instance
   */
  public static HttpServletRequest getThreadLocalRequest() {
    return perThreadRequest.get();
  }

  /**
   * Returns the thread-local {@link HttpServletResponse}.
   * 
   * @return an {@link HttpServletResponse} instance
   */
  public static HttpServletResponse getThreadLocalResponse() {
    return perThreadResponse.get();
  }

  /**
   * Returns the thread-local {@link ServletContext}
   * 
   * @return the {@link ServletContext} associated with this servlet
   */
  public static ServletContext getThreadLocalServletContext() {
    return perThreadContext.get();
  }

  @Inject
  private SimpleRequestProcessor processor;

  /**
   * Processes a POST to the server.
   * 
   * @param request an {@link HttpServletRequest} instance
   * @param response an {@link HttpServletResponse} instance
   * @throws IOException if an internal I/O error occurs
   * @throws ServletException if an error occurs in the servlet
   */
  @Override
  protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException, ServletException {

    perThreadContext.set(getServletContext());
    perThreadRequest.set(request);
    perThreadResponse.set(response);

    // No new code should be placed outside of this try block.
    try {
      String jsonRequestString =
          RPCServletUtils.readContent(request, JSON_CONTENT_TYPE, JSON_CHARSET);
      if (DUMP_PAYLOAD) {
        System.out.println(">>> " + jsonRequestString);
      }

      try {
        String payload = processor.process(jsonRequestString);
        if (DUMP_PAYLOAD) {
          System.out.println("<<< " + payload);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(RequestManager.JSON_CONTENT_TYPE_UTF8);
        response.setContentLength(payload.getBytes(JSON_CHARSET).length);
        // The Writer must be obtained after setting the content type
        PrintWriter writer = response.getWriter();
        writer.print(payload);
        writer.flush();
      } catch (RuntimeException e) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        log.log(Level.SEVERE, "Unexpected error", e);
      }
    } finally {
      perThreadContext.set(null);
      perThreadRequest.set(null);
      perThreadResponse.set(null);
    }
  }

}
