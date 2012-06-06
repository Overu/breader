/**
 * Documentation here.
 */
package com.goodow.web.example.server;

import java.util.UUID;

import javax.annotation.Generated;

import com.goodow.web.core.server.ServerService;
import com.goodow.web.example.shared.Library;
import com.goodow.web.example.shared.LibraryService;
import com.google.inject.persist.Transactional;

/**
 * Documentation 22.
 */
@Generated("cloudlet")
public class ServerLibraryService extends ServerService implements LibraryService {
  @Override
  @Transactional
  public Library save(final Library library, final boolean flag, final int size) {
    if (library.getId() == null) {
      library.setId(UUID.randomUUID().toString());
    }
    em().persist(library);
    return library;
  }
}
