package com.goodow.web.reader.client;

import com.goodow.web.reader.shared.*;
import com.goodow.web.core.client.*;
import com.goodow.web.core.shared.*;
public class ClientLibraryService extends ClientService<Library> {
  public Request<Library> save(Library library, boolean flag, int size) {
    return invoke(ExamplePackage.LibraryService_save, library, flag, size);
  }
}
