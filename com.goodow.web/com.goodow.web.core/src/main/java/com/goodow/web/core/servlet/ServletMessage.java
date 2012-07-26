/* ***************************************************************** */
/*                                                                   */
/* IBM Confidential */
/*                                                                   */
/* OCO Source Materials */
/*                                                                   */
/* Copyright IBM Corp. 2011 */
/*                                                                   */
/* The source code for this program is not published or otherwise */
/* divested of its trade secrets, irrespective of what has been */
/* deposited with the U.S. Copyright Office. */
/*                                                                   */
/* ***************************************************************** */

package com.goodow.web.core.servlet;

import com.goodow.web.core.shared.Resource;

import com.google.appengine.api.log.InvalidRequestException;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletMessage {

  private static final Logger logger = Logger.getLogger(ServletMessage.class.getName());

  private static final String TRUE = "true";

  private static final String FALSE = "false";

  public static Resource createResource(final InputStream is, final String fileName,
      final String mimeType) throws IOException {
    Resource resource = new Resource();
    UUID uuid = UUID.randomUUID();
    resource.setId(uuid.toString());
    resource.setFileName(fileName);
    resource.setMimeType(mimeType);
    String filePath = "D:/DevData/resource/" + resource.getId();
    resource.setPath(filePath);
    InputStream in = null;
    OutputStream out = null;
    try {
      File file = new File(filePath);
      // // file.mkdirs();
      file.getParentFile().mkdirs();
      file.createNewFile();
      in = new BufferedInputStream(is);
      out = new FileOutputStream(filePath);
      byte[] buffer = new byte[1024];
      for (int bytesRead = in.read(buffer); bytesRead > 0; bytesRead = in.read(buffer)) {
        out.write(buffer, 0, bytesRead);
      }
    } finally {
      IOUtils.closeQuietly(in);
      IOUtils.closeQuietly(out);
    }
    return resource;
  }

  public static void writeResource(final Resource resource, final OutputStream out) throws IOException {
    String filePath = "D:/DevData/resource/" + resource.getId();
    InputStream in = null;
    try {
      File file = new File(filePath);
      in = new BufferedInputStream(new FileInputStream(file));
      byte[] buffer = new byte[1024];
      for (int bytesRead = in.read(buffer); bytesRead > 0; bytesRead = in.read(buffer)) {
        out.write(buffer, 0, bytesRead);
      }
    } finally {
      IOUtils.closeQuietly(in);
      IOUtils.closeQuietly(out);
    }
  }

  private HttpServletRequest servletRequest;

  private HttpServletResponse servletResponse;

  private Map<String, Object> stringParamValues;

  private Map<String, Resource> transactionalFiles;

  private boolean multipartParamsParsed = false;

  private static final String KEY = ServletMessage.class.getName();

  /**
   * Create a message instance per request/response.
   */
  public static ServletMessage create(final HttpServletRequest request,
      final HttpServletResponse response) {
    ServletMessage result = from(request);
    if (result == null) {
      result = new ServletMessage(request, response);
      request.setAttribute(KEY, result);
    }
    return result;
  }

  /**
   * Return the message instance associated with the given request.
   */
  public static ServletMessage from(final HttpServletRequest request) {
    return (ServletMessage) request.getAttribute(KEY);
  }

  private ServletMessage(final HttpServletRequest request, final HttpServletResponse response) {
    this.servletRequest = request;
    this.servletResponse = response;
    stringParamValues = new HashMap<String, Object>(6);
    transactionalFiles = new HashMap<String, Resource>(2);
  }

  /**
   * Add string value to parameter of given name (for multivalues), if not null.
   */
  public void addParameter(final String name, final String value) {
    if (value != null) {
      Object oldValue = stringParamValues.get(name);
      if (oldValue == null) {
        stringParamValues.put(name, value);
      } else {
        List<String> values;
        if (oldValue instanceof String) {
          values = new ArrayList<String>(2);
          values.add((String) oldValue);
          stringParamValues.put(name, values);
        } else {
          values = (List<String>) oldValue;
        }
        values.add(value);
      }
    }
  }

  /**
   * Get parameter value as boolean. Return null if not specified or the value is invalid.
   */
  public Boolean getBoolean(final String paramName) {
    String strValue = getString(paramName);
    if (TRUE.equalsIgnoreCase(strValue)) {
      return true;
    }
    if (FALSE.equalsIgnoreCase(strValue)) {
      return false;
    }
    return null;
  }

  /**
   * Get parameter value as boolean. Return default value if not specified or the valid is invalid.
   */
  public boolean getBoolean(final String paramName, final boolean defaultValue) {
    Boolean value = getBoolean(paramName);
    if (value == null) {
      return defaultValue;
    } else {
      return value.booleanValue();
    }
  }

  /**
   * Get parameter value as Resource. Return null if not specified.
   */
  public Resource getFile(final String paramName) {
    return transactionalFiles.get(paramName);
  }

  /**
   * Get parameter value as integer. Return null if not specified.
   */
  public Integer getInt(final String paramName) throws InvalidRequestException {
    try {
      String strValue = getString(paramName);
      return strValue == null ? null : Integer.valueOf(strValue);
    } catch (NumberFormatException e) {
      if (logger.isLoggable(Level.SEVERE)) {
        logger.logp(Level.SEVERE, getClass().getName(), "getInt", e.getMessage(), e);
      }
      throw e;
    }
  }

  /**
   * Get parameter value as integer. Return default value if not specified.
   */
  public int getInt(final String paramName, final int defaultValue) throws InvalidRequestException {
    Integer value = getInt(paramName);
    return value == null ? defaultValue : value.intValue();
  }

  /**
   * Get parameter value as long. Return null if not specified.
   */
  public Long getLong(final String paramName) throws InvalidRequestException {
    try {
      String strValue = getString(paramName);
      return strValue == null ? null : Long.valueOf(strValue);
    } catch (NumberFormatException e) {
      if (logger.isLoggable(Level.SEVERE)) {
        logger.logp(Level.SEVERE, getClass().getName(), "getLong", e.getMessage(), e);
      }
      throw e;
    }
  }

  /**
   * Get parameter value as long. Return default value if not specified.
   */
  public long getLong(final String paramName, final int defaultValue)
      throws InvalidRequestException {
    Long value = getLong(paramName);
    return value == null ? defaultValue : value.longValue();
  }

  /**
   * Get parameter value as string. Return null if not specified.
   */
  public String getString(final String paramName) {
    Object result = stringParamValues.get(paramName);
    if (result == null) {
      return servletRequest.getParameter(paramName);
    } else if (result instanceof List) {
      return ((List<String>) result).get(0);
    } else {
      return (String) result;
    }
  }

  /**
   * Get parameter value as string. Return default value if not specified.
   */
  public String getString(final String paramName, final String defaultValue) {
    String result = getString(paramName);
    return result == null ? defaultValue : result;
  }

  /**
   * Get parameter as string list. Return null if not specified. If the parameter is also specified
   * by multipart form fields or atom elements, return it in addition to query parameter values.
   */
  public List<String> getStringList(final String paramName) {
    List<String> result;
    Object value = stringParamValues.get(paramName);
    String[] additionalValues = servletRequest.getParameterValues(paramName);
    if (value == null) {
      if (additionalValues == null) {
        return null;
      } else {
        result = Arrays.asList(additionalValues);
      }
    } else {
      if (value instanceof String) {
        result = Arrays.asList(new String[] {(String) value});
      } else {
        result = (List<String>) value;
      }
      if (additionalValues != null) {
        // Arrays.asList returns Arrays.ArrayList, not java.util.AraryList
        // Arrays.ArrayList does not support addAll();
        result = new ArrayList<String>(result);
        result.addAll(Arrays.asList(additionalValues));
      }
    }
    return Collections.unmodifiableList(result);
  }

  /**
   * Get parameter as string values. Return null if not specified. If the parameter is also
   * specified by multipart form fields or atom elements, return it in addition to query parameter
   * values.
   */
  public String[] getStringValues(final String paramName) {
    Object value = stringParamValues.get(paramName);
    String[] additionalValues = servletRequest.getParameterValues(paramName);
    if (value == null) {
      return additionalValues;
    } else if (additionalValues == null) {
      if (value instanceof String) {
        return new String[] {(String) value};
      } else {
        List<String> values = (List<String>) value;
        return values.toArray(new String[values.size()]);
      }
    } else {
      String[] result;
      int size;
      if (value instanceof String) {
        size = 1;
        result = new String[1 + additionalValues.length];
        result[0] = (String) value;
      } else {
        List<String> values = (List<String>) value;
        size = values.size();
        result = new String[size + additionalValues.length];
        values.toArray(result);
      }
      for (int i = 0; i < additionalValues.length; i++) {
        result[i + size] = additionalValues[i];
      }
      return result;
    }
  }

  /**
   * Get parameter value as time. Return null if not specified. Throw exception if parameter value
   * is invalid.
   */
  public Timestamp getTime(final String paramName) throws InvalidRequestException {
    String dateString = getString(paramName);
    Timestamp date = null;
    if (dateString != null && dateString.length() > 0) {
      try {
        date = new Timestamp(Long.valueOf(dateString));
      } catch (NumberFormatException e) {
        if (logger.isLoggable(Level.SEVERE)) {
          logger.logp(Level.SEVERE, getClass().getName(), "getTime", e.getMessage(), e);
        }
        throw e;
      }
    }
    return date;
  }

  public Resource parseFile(final String paramName, final InputStream is, final String fileName,
      final String contentType) throws Exception {
    Resource resource = createResource(is, fileName, contentType);
    setParameter(paramName, resource);
    return resource;
  }

  /**
   * Parse parameter value from servletRequest header.
   */
  public String parseHeaderParam(final String paramName, final String headerName) throws Exception {
    String value = servletRequest.getHeader(headerName);
    if (value != null) {
      value = MimeUtility.decodeText(value);
    }
    setParameter(paramName, value);
    return value;
  }

  /**
   * Extract parameters from multipart form data. This must be called in the context of a
   * transaction if we are expecting file items.
   */
  public ServletMessage parseMultipartParams(final long maximumFileSize) throws Exception {
    // Prevent from being called repeatedly
    if (multipartParamsParsed) {
      return this;
    }
    multipartParamsParsed = true;

    ServletFileUpload servletFileUpload = new ServletFileUpload();
    // look for opId, if opId is present, then do progress listening
    String opId = servletRequest.getParameter("op");
    String ssId = null;
    boolean isListening = opId != null && opId.length() > 0;
    if (isListening) {
      ssId = "op" + opId;
      FileUploadProgressListener fileUploadProgressListener = new FileUploadProgressListener();
      servletRequest.getSession().setAttribute(ssId, fileUploadProgressListener);
      servletFileUpload.setProgressListener(fileUploadProgressListener);
    }

    if (maximumFileSize != -1) {
      servletFileUpload.setFileSizeMax(maximumFileSize);
    }

    try {
      FileItemIterator iter = servletFileUpload.getItemIterator(servletRequest);
      while (iter.hasNext()) {
        FileItemStream value = iter.next();
        String key = value.getFieldName();
        InputStream in = value.openStream();
        try {
          if (value.isFormField()) {
            String strValue = Streams.asString(in, "UTF-8");
            addParameter(key, strValue);
          } else {
            parseFile(key, in, value.getName(), value.getContentType());
          }
        } finally {
          IOUtils.closeQuietly(in);
        }
      }
    } catch (Exception e) {
      // VirusFoundException, VirusFoundException will be handled by ServiceEndPointUtil centrally
      if (e.getCause() != null
          && e.getCause() instanceof FileUploadBase.FileSizeLimitExceededException) {
        throw (FileUploadBase.FileSizeLimitExceededException) e.getCause();
      } else if (e instanceof FileUploadBase.SizeLimitExceededException) {
        throw e;
      } else {
        throw e;
      }
    } finally {
      // clean up the session if we had a listener
      if (ssId != null) {
        servletRequest.getSession().removeAttribute(ssId);
      }
    }
    return this;
  }

  /**
   * Set parameter of given name to transactional file.
   */
  public void setParameter(final String name, final Resource file) {
    transactionalFiles.put(name, file);
  }

  /**
   * Set parameter of given name to string value, if not null.
   */
  public void setParameter(final String name, final String value) {
    if (value != null) {
      stringParamValues.put(name, value);
    }
  }

}
