package com.goodow.web.core.servlet;

import com.goodow.web.core.server.ServerMessage;
import com.goodow.web.core.shared.Message;

import com.google.gwt.user.server.rpc.RPCServletUtils;
import com.google.inject.Inject;
import com.google.inject.Provider;
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

@SuppressWarnings("serial")
@Singleton
public class WebServiceServlet extends HttpServlet {

  public static final String END_POINT = "rpc";

  private static final boolean DUMP_PAYLOAD = Boolean.getBoolean("gwt.rpc.dumpPayload");
  private static final String JSON_CHARSET = "UTF-8";
  private static final String JSON_CONTENT_TYPE = "application/json";
  private static final Logger log = Logger.getLogger(WebServiceServlet.class.getCanonicalName());

  private static final ThreadLocal<ServletContext> perThreadContext =
      new ThreadLocal<ServletContext>();
  private static final ThreadLocal<HttpServletRequest> perThreadRequest =
      new ThreadLocal<HttpServletRequest>();
  private static final ThreadLocal<HttpServletResponse> perThreadResponse =
      new ThreadLocal<HttpServletResponse>();

  public static HttpServletRequest getThreadLocalRequest() {
    return perThreadRequest.get();
  }

  public static HttpServletResponse getThreadLocalResponse() {
    return perThreadResponse.get();
  }

  public static ServletContext getThreadLocalServletContext() {
    return perThreadContext.get();
  }

  @Inject
  private Provider<ServerMessage> message;

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
        String payload = message.get().process(jsonRequestString);
        if (DUMP_PAYLOAD) {
          System.out.println("<<< " + payload);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(Message.JSON_CONTENT_TYPE_UTF8);
        response.setContentLength(payload.getBytes(JSON_CHARSET).length);
        // The Writer must be obtained after setting the content type
        PrintWriter writer = response.getWriter();
        writer.print(payload);
        writer.flush();
      } catch (Exception e) {
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
