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
package com.goodow.wave.server.media;

import com.goodow.wave.server.util.Util;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.conversion.Asset;
import com.google.appengine.api.conversion.Conversion;
import com.google.appengine.api.conversion.ConversionResult;
import com.google.appengine.api.conversion.ConversionServiceFactory;
import com.google.appengine.api.conversion.Document;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.gooodow.wave.shared.media.MimeType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class ConversionService {

  @Inject
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  static final Logger logger = Logger.getLogger(ConversionService.class.getName());

  public List<BlobKey> convertFromPdfToPng(final BlobKey blobKey) throws IOException {
    logger.info("0");
    BlobstoreInputStream blobstoreInputStream = new BlobstoreInputStream(blobKey);
    logger.info("0.1" + blobstoreInputStream);
    byte[] bytes = Util.readStreamAsBytes(blobstoreInputStream);
    logger.info("0.5" + bytes);
    Asset asset = new Asset(MimeType.APPLICATION_PDF.getType(), bytes);
    logger.info("1" + asset);
    Document document = new Document(asset);
    // ConversionOptions options =
    // ConversionOptions.Builder.withImageWidth(1000).firstPage(2).lastPage(10);
    Conversion conversion = new Conversion(document, MimeType.IMAGE_PNG.getType());
    com.google.appengine.api.conversion.ConversionService conversionService =
        ConversionServiceFactory.getConversionService();
    logger.info("2");
    ConversionResult result = conversionService.convert(conversion);

    if (result.success()) {
      logger.info("Conversion success");
      List<BlobKey> toReturn = new ArrayList<BlobKey>();
      for (Asset a : result.getOutputDoc().getAssets()) {
        toReturn.add(createNewBlobFile("", a.getData()));
      }
      return toReturn;
    } else {
      logger.warning("Conversion ErrorCode:" + result.getErrorCode());
      throw new IllegalStateException("格式转换出错");
    }
  }

  private BlobKey createNewBlobFile(final String mimeType, final byte[] bytes) throws IOException {
    FileService fileService = FileServiceFactory.getFileService();
    AppEngineFile file = fileService.createNewBlobFile(mimeType);
    logger.info("createNewBlobFile");
    boolean lock = false;
    FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);
    writeChannel.write(ByteBuffer.wrap(bytes));
    logger.info("3");
    writeChannel.closeFinally();
    return fileService.getBlobKey(file);
  }
}
