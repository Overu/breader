/**
 * Documentation here.
 */
package com.goodow.web.reader.jpa;

import com.goodow.web.core.jpa.JpaWebContentService;
import com.goodow.web.reader.shared.Library;
import com.goodow.web.reader.shared.LibraryService;

import com.google.inject.persist.Transactional;

import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

/**
 * Documentation 22.
 */
@Generated("cloudlet")
public class JpaLibraryService extends JpaWebContentService<Library> implements LibraryService {
  @Override
  public Library getMyLibrary() {
    List<Library> result =
        em().createQuery("select b from Library b where b.owner=null", Library.class)
            .getResultList();
    if (result.isEmpty()) {
      Library library = new Library();
      save(library, false, 0);
      return library;
    } else {
      return result.get(0);
    }
  }

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
