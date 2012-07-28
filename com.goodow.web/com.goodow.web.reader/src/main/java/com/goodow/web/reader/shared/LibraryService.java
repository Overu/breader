package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebEntityService;

public interface LibraryService extends WebEntityService<Library> {

  Library save(Library library, final boolean flag, final int size);

}
