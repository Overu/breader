package com.goodow.web.mvp.shared;

import com.goodow.web.core.shared.KeyUtil;
import com.goodow.web.feature.client.FeatureDetection;

import com.google.gwt.core.client.Callback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.google.web.bindery.requestfactory.shared.DefaultProxyStore;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxySerializer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import com.googlecode.gwtphonegap.client.file.DirectoryEntry;
import com.googlecode.gwtphonegap.client.file.File;
import com.googlecode.gwtphonegap.client.file.FileCallback;
import com.googlecode.gwtphonegap.client.file.FileEntry;
import com.googlecode.gwtphonegap.client.file.FileError;
import com.googlecode.gwtphonegap.client.file.FileReader;
import com.googlecode.gwtphonegap.client.file.FileSystem;
import com.googlecode.gwtphonegap.client.file.FileWriter;
import com.googlecode.gwtphonegap.client.file.Flags;
import com.googlecode.gwtphonegap.client.file.ReaderCallback;
import com.googlecode.gwtphonegap.client.file.WriterCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class FileProxyStore {
  /**
   * 
   */
  private static final String ROOT_PROXY_KEY = "root";

  private static final int SIZE = 500 * 1024 * 1024;

  private final static Logger logger = Logger.getLogger(FileProxyStore.class.getName());

  private static native void requestQuota(int fileSystemType, int size,
      Callback<Integer, Object> callback)/*-{
                                                   $wnd.requestFileSystem  = window.requestFileSystem || window.webkitRequestFileSystem;
                                                   var fail = function(error) {
                                                   $entry(callback.@com.google.gwt.core.client.Callback::onFailure(Ljava/lang/Object;)(error));
                                                   };

                                                   var suc = function(grantedBytes) {
                                                   $entry(@com.goodow.web.mvp.shared.FileProxyStore::requestQuotaOnSuccess(ILcom/google/gwt/core/client/Callback;)(grantedBytes,callback));
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

  private static final Flags putFlags = new Flags(true, false);
  private static final Flags getFlags = new Flags(false, false);

  private final LocalStorage localStorage;

  private final KeyUtil keyUtil;

  private final RequestFactory f;

  @Inject
  FileProxyStore(final File file, final LocalStorage localStorage, final KeyUtil keyUtil,
      final RequestFactory f) {
    this.file = file;
    this.localStorage = localStorage;
    this.keyUtil = keyUtil;
    this.f = f;

    if (FeatureDetection.mobileNative()) {
      callback.onSuccess(SIZE);
    } else if (FeatureDetection.fileSystem()) {
      requestQuota(FileSystem.LocalFileSystem_PERSISTENT, SIZE, callback);
    }
  }

  public <V> void get(final String key, final Callback<V, Object> callback) {
    rootDirectory.getFile(key, getFlags, new FileCallback<FileEntry, FileError>() {

      @Override
      public void onFailure(final FileError error) {
        callback.onFailure(error);
      }

      @Override
      public void onSuccess(final FileEntry fileEntry) {
        if (fileEntry == null) {
          callback.onSuccess(null);
        }
        FileReader reader = file.createReader();
        reader.setOnLoadEndCallback(new ReaderCallback<FileReader>() {

          @Override
          public void onCallback(final FileReader result) {
            String payload = result.getResult();
            DefaultProxyStore proxyStore = new DefaultProxyStore(payload);
            ProxySerializer serializer = f.getSerializer(proxyStore);
            V proxy =
                (V) serializer.deserialize(f
                    .getProxyId(proxyStore.get(ROOT_PROXY_KEY).getPayload()));
            callback.onSuccess(proxy);
          }
        });

        reader.setOnErrorCallback(new ReaderCallback<FileReader>() {

          @Override
          public void onCallback(final FileReader result) {
            callback.onFailure(result);
          }
        });
        reader.readAsText(fileEntry);
      }
    });
  }

  public DirectoryEntry getRootDirectory() {
    return rootDirectory;
  }

  public void put(final String key, final EntityProxy proxy, final Callback<Void, Object> callback) {
    rootDirectory.getFile(key, putFlags, new FileCallback<FileEntry, FileError>() {

      @Override
      public void onFailure(final FileError error) {
        logger.log(Level.SEVERE, "出错了\n" + error);
        callback.onFailure(error);
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
                callback.onFailure(result);
              }
            });
            fileWriter.setOnWriteEndCallback(new WriterCallback<FileWriter>() {

              @Override
              public void onCallback(final FileWriter result) {
                callback.onSuccess(null);
              }
            });
            DefaultProxyStore proxyStore = new DefaultProxyStore();
            String historyToken = f.getHistoryToken(proxy.stableId());
            proxyStore.put(ROOT_PROXY_KEY, StringQuoter.split(historyToken));
            ProxySerializer serializer = f.getSerializer(proxyStore);
            serializer.serialize(proxy);
            fileWriter.write(proxyStore.encode());
          }
        });
      }
    });
  }

}
