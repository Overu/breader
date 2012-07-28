/**
 * Documentation here.
 */
package com.goodow.web.reader.jpa;

import com.goodow.web.core.jpa.JpaEntityService;
import com.goodow.web.reader.shared.Library;
import com.goodow.web.reader.shared.LibraryService;

import com.google.inject.persist.Transactional;

import java.util.UUID;

import javax.annotation.Generated;

/**
 * Documentation 22.
 */
@Generated("cloudlet")
public class JpaLibraryService extends JpaEntityService<Library> implements LibraryService {
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
