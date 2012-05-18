/*
 * Copyright 2011 Google Inc. All Rights Reserved.
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

package com.goodow.wave.server.attachment;

import com.google.appengine.repackaged.com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Serves attachment metadata as JSON.
 * 
 * We support POST because the client could legitimately ask for a very large number of attachment
 * ids at once, which may not fit into a GET request.
 * 
 * At the same time, we should support GET because it is appropriate for small requests and
 * convenient for debugging.
 * 
 * @author danilatos@google.com (Daniel Danilatos)
 */
@Singleton
public class AttachmentMetadataHandler extends HttpServlet {
  /** Avoid doing too much work at once so the client can get incremental being loaded */
  private static final long MAX_REQUEST_TIME_MS = 4 * 1000;
  /**
   * Prefix added to data returned from XHRs to guard against XSSI attacks.
   * 
   * See http://google-gruyere.appspot.com/part4
   */
  public static final String XSSI_PREFIX = "])}while(1);</x>//";
  private static final String INVALID_ATTACHMENT_ID_METADATA_STRING = "{}";

  private final AttachmentService attachments;

  @Inject
  public AttachmentMetadataHandler(final AttachmentService attachments) {
    this.attachments = attachments;
  }

  @Override
  public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws IOException {
    doRequest(req, resp);
  }

  @Override
  public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws IOException {
    doRequest(req, resp);
  }

  private void doRequest(final HttpServletRequest req, final HttpServletResponse resp)
      throws IOException {
    Map<String, Optional<AttachmentMetadata>> result = null;
    // attachments.getMetadata(getIds(req), MAX_REQUEST_TIME_MS);
    JSONObject json = new JSONObject();
    try {
      for (Entry<String, Optional<AttachmentMetadata>> entry : result.entrySet()) {
        JSONObject metadata =
            new JSONObject(entry.getValue().isPresent() ? entry.getValue().get()
                .getMetadataJsonString() : INVALID_ATTACHMENT_ID_METADATA_STRING);
        String queryParams = "attachment=" + entry.getKey();
        metadata.put("url", "/download?" + queryParams);
        metadata.put("thumbnailUrl", "/thumbnail?" + queryParams);
        json.put(entry.getKey(), metadata);
      }
    } catch (JSONException e) {
      throw new Error(e);
    }
    resp.getWriter().print(XSSI_PREFIX + "(" + json.toString() + ")");
  }

  private List<String> getIds(final HttpServletRequest req) {
    List<String> out = new ArrayList<String>();
    for (String id : req.getParameter("ids").split(",", -1)) {
      out.add(id);
    }
    return out;
  }
}
