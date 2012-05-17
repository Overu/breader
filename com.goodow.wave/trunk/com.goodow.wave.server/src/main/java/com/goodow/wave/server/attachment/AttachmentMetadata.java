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

import com.google.appengine.api.blobstore.BlobKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Metadata associated with an attachment. Should not be changed for a given attachment, so that it
 * can be cached indefinitely.
 * 
 * @author danilatos@google.com (Daniel Danilatos)
 */
// TODO(danilatos) Merge this with WalkaroundAttachment, once we have a json interface.
// Same goes for "ImageMetadata"
// TODO(danilatos) Consider using a proto-based implementation?
@Entity
public class AttachmentMetadata implements Serializable {
  public interface ImageMetadata {
    public int getHeight();

    public int getWidth();
  }

  private static final long serialVersionUID = 562560590763783822L;
  @Id
  private String id;
  private String blobKey;
  // String for Serializable
  private String rawMetadata;
  // Parsed version of rawMetadata.
  private transient JSONObject maybeMetadata;

  public AttachmentMetadata() {
  }

  public AttachmentMetadata(final AttachmentId id, final BlobKey blobKey, final JSONObject metadata) {
    this.id = id.getId();
    this.blobKey = blobKey.getKeyString();
    this.maybeMetadata = metadata;
    this.rawMetadata = metadata.toString();
  }

  public BlobKey getBlobKey() {
    return new BlobKey(blobKey);
  }

  public AttachmentId getId() {
    return new AttachmentId(id);
  }

  public ImageMetadata getImage() {
    return createImageMetadata("image");
  }

  public String getMetadataJsonString() {
    return rawMetadata;
  }

  public ImageMetadata getThumbnail() {
    return createImageMetadata("thumbnail");
  }

  @Override
  public String toString() {
    return "AttachmentMetadata(" + id + ", " + blobKey + ", " + rawMetadata + ")";
  }

  private ImageMetadata createImageMetadata(final String key) {
    final JSONObject imgData;
    try {
      imgData = getMetadata().has(key) ? getMetadata().getJSONObject(key) : null;
    } catch (JSONException e) {
      throw new Error(e);
    }

    if (imgData == null) {
      return null;
    }

    return new ImageMetadata() {
      @Override
      public int getHeight() {
        try {
          return imgData.getInt("height");
        } catch (JSONException e) {
          throw new RuntimeException(e);
        }
      }

      @Override
      public int getWidth() {
        try {
          return imgData.getInt("width");
        } catch (JSONException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  private JSONObject getMetadata() {
    if (maybeMetadata == null) {
      assert rawMetadata != null : "rawMetadata is null";
      try {
        maybeMetadata = new JSONObject(rawMetadata);
      } catch (JSONException e) {
        throw new RuntimeException("Bad JSON: " + rawMetadata, e);
      }
    }
    return maybeMetadata;
  }

}
