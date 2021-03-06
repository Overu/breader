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
import com.google.appengine.api.conversion.Asset;
import com.google.appengine.api.conversion.Conversion;
import com.google.appengine.api.conversion.ConversionResult;
import com.google.appengine.api.conversion.ConversionServiceFactory;
import com.google.appengine.api.conversion.Document;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.inject.Singleton;

import com.gooodow.wave.shared.media.MimeType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class ConversionService {

  static final Logger logger = Logger.getLogger(ConversionService.class.getName());

  public List<String> convertFromPdfToPng(final BlobKey blobKey) throws IOException {
    BlobstoreInputStream blobstoreInputStream = new BlobstoreInputStream(blobKey);
    byte[] bytes = Util.readStreamAsBytes(blobstoreInputStream);
    Asset asset = new Asset(MimeType.APPLICATION_PDF.getType(), bytes, "test.pdf");
    Document document = new Document(asset);
    // ConversionOptions options =
    // ConversionOptions.Builder.withImageWidth(1000).firstPage(2).lastPage(10);
    Conversion conversion = new Conversion(document, MimeType.IMAGE_PNG.getType());
    com.google.appengine.api.conversion.ConversionService conversionService =
        ConversionServiceFactory.getConversionService();
    ConversionResult result = null;
    try {
      result = conversionService.convert(conversion);
      logger.info("2.1" + result);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "转换异常", e);
    }

    if (result.success()) {
      logger.info("Conversion success");
      List<String> toReturn = new ArrayList<String>();
      List<Asset> assets = result.getOutputDoc().getAssets();
      for (int i = 0; i < assets.size(); i++) {
        Asset a = assets.get(i);
        toReturn.add(createNewBlobFile(MimeType.IMAGE_PNG.getType(), "" + i, a.getData()));
      }
      return toReturn;
    } else {
      logger.warning("Conversion ErrorCode:" + result.getErrorCode());
      throw new IllegalStateException("格式转换出错");
    }
  }

  private String createNewBlobFile(final String mimeType, final String blobInfoUploadedFileName,
      final byte[] bytes) throws IOException {
    FileService fileService = FileServiceFactory.getFileService();
    AppEngineFile file = null;
    try {
      file = fileService.createNewBlobFile(mimeType, blobInfoUploadedFileName);
      FileWriteChannel writeChannel = fileService.openWriteChannel(file, true);
      writeChannel.write(ByteBuffer.wrap(bytes));
      writeChannel.close();
      logger.info("close:" + blobInfoUploadedFileName);
      writeChannel.closeFinally();
      logger.info("closeFinally" + blobInfoUploadedFileName);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "关闭writeChannel时异常", e);
    }
    return fileService.getBlobKey(file).getKeyString();
  }
}
