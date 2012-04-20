package org.cloudlet.web.service.shared;

import com.goodow.web.feature.client.FeatureDetection;

import com.google.gwt.core.client.Callback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.googlecode.gwtphonegap.client.file.DirectoryEntry;
import com.googlecode.gwtphonegap.client.file.File;
import com.googlecode.gwtphonegap.client.file.FileCallback;
import com.googlecode.gwtphonegap.client.file.FileEntry;
import com.googlecode.gwtphonegap.client.file.FileError;
import com.googlecode.gwtphonegap.client.file.FileSystem;
import com.googlecode.gwtphonegap.client.file.FileWriter;
import com.googlecode.gwtphonegap.client.file.Flags;
import com.googlecode.gwtphonegap.client.file.WriterCallback;

import org.cloudlet.web.service.shared.rpc.ResourceProxy;

import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class FileProxyStore {
  private static final int SIZE = 500 * 1024 * 1024;

  private final static Logger logger = Logger.getLogger(FileProxyStore.class.getName());

  private static native void requestQuota(int fileSystemType, int size,
      Callback<Integer, Object> callback)/*-{
                                                   $wnd.requestFileSystem  = window.requestFileSystem || window.webkitRequestFileSystem;
                                                   var fail = function(error) {
                                                   $entry(callback.@com.google.gwt.core.client.Callback::onFailure(Ljava/lang/Object;)(error));
                                                   };

                                                   var suc = function(grantedBytes) {
                                                   $entry(@org.cloudlet.web.service.shared.FileProxyStore::requestQuotaOnSuccess(ILcom/google/gwt/core/client/Callback;)(grantedBytes,callback));
                                                   };

                                                   $wnd.webkitStorageInfo.requestQuota(fileSystemType, size, $entry(suc), $entry(fail));

                                                   }-*/;

  private static void requestQuotaOnSuccess(final int size, final Callback<Integer, Object> callback) {
    callback.onSuccess(size);
  }

  private FileSystem fileSystem;
  private DirectoryEntry rootDirectory;
  private final File file;
  private final Callback<Integer, Object> callback = new Callback<Integer, Object>() {

    @Override
    public void onFailure(final Object reason) {
      logger.log(Level.SEVERE, "请求持久存储空间时出错(" + SIZE + " bytes)\n" + reason);
    }

    @Override
    public void onSuccess(final Integer size) {
      file.requestFileSystem(FileSystem.LocalFileSystem_PERSISTENT, size.intValue(),
          new FileCallback<FileSystem, FileError>() {

            @Override
            public void onFailure(final FileError error) {
              logger.log(Level.SEVERE, "获取文件系统时出错(ErrorCode=" + error.getErrorCode() + ")\n"
                  + error);
            }

            @Override
            public void onSuccess(final FileSystem entry) {
              fileSystem = entry;
              rootDirectory = fileSystem.getRoot();
            }
          });
    }
  };

  private static final Flags flags = new Flags(true, false);

  private final LocalStorage localStorage;

  @Inject
  FileProxyStore(final File file, final LocalStorage localStorage) {
    this.file = file;
    this.localStorage = localStorage;

    if (!FeatureDetection.mobileNative()) {
      requestQuota(FileSystem.LocalFileSystem_PERSISTENT, SIZE, callback);
    } else {
      callback.onSuccess(SIZE);
    }
  }

  public DirectoryEntry getRootDirectory() {
    return rootDirectory;
  }

  public void put(final String key, final ResourceProxy content,
      final Callback<Void, Object> callback) {
    rootDirectory.getFile(key, flags, new FileCallback<FileEntry, FileError>() {

      @Override
      public void onFailure(final FileError error) {
        logger.log(Level.SEVERE, "出错了\n" + error);
      }

      @Override
      public void onSuccess(final FileEntry fileEntry) {
        fileEntry.createWriter(new FileCallback<FileWriter, FileError>() {

          @Override
          public void onFailure(final FileError error) {
            logger.log(Level.SEVERE, "出错了\n" + error);
          }

          @Override
          public void onSuccess(final FileWriter fileWriter) {
            fileWriter.setOnErrorCallback(new WriterCallback<FileWriter>() {

              @Override
              public void onCallback(final FileWriter result) {
                logger.log(Level.SEVERE, "出错了\n" + result);
              }
            });
            fileWriter.setOnWriteEndCallback(new WriterCallback<FileWriter>() {

              @Override
              public void onCallback(final FileWriter result) {
                callback.onSuccess(null);
              }
            });
            String payload = localStorage.encode(content).getPayload();
            fileWriter.write(payload);
          }
        });
      }
    });
  }

}
