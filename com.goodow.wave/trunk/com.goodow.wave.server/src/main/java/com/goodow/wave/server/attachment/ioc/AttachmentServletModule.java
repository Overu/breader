/*
 * Copyright 2012 Goodow.com
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
package com.goodow.wave.server.attachment.ioc;

import com.goodow.wave.server.attachment.AttachmentMetadataHandler;
import com.goodow.wave.server.attachment.AttachmentUploadHandler;

import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;

public class AttachmentServletModule extends ServletModule {
  @Override
  protected void configureServlets() {
    super.configureServlets();
    serve("/attachmentinfo").with(AttachmentMetadataHandler.class);
    serve("/upload").with(AttachmentUploadHandler.class);
  }

  @Provides
  BlobInfoFactory provideBlobInfoFactory() {
    return new BlobInfoFactory();
  }

  @Provides
  BlobstoreService provideBlobstoreService() {
    return BlobstoreServiceFactory.getBlobstoreService();
  }

  @Provides
  ImagesService provideImagesService() {
    return ImagesServiceFactory.getImagesService();
  }

  @Provides
  URLFetchService provideUrlFetchService() {
    return URLFetchServiceFactory.getURLFetchService();
  }
}
