package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebContentService;

public interface LibraryService extends WebContentService<Library> {

  Library save(Library library, final boolean flag, final int size);

}
