package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebService;

public interface LibraryService extends WebService<Library> {

  Library save(Library library, final boolean flag, final int size);

}
