package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.ContentService;
import com.goodow.web.core.shared.WebService;

@WebService
public interface LibraryService extends ContentService<Library> {

  Library save(Library library, final boolean flag, final int size);

}
