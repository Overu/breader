/**
 * Documentation here.
 */
package com.goodow.web.example.server;

import com.goodow.web.example.shared.Library;
import com.goodow.web.example.shared.LibraryService;
import com.goodow.web.security.server.ServerContentService;

import com.google.inject.persist.Transactional;

import java.util.UUID;

import javax.annotation.Generated;

/**
 * Documentation 22.
 */
@Generated("cloudlet")
public class ServerLibraryService extends ServerContentService<Library> implements LibraryService {
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
