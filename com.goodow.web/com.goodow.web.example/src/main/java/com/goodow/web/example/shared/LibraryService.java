package com.goodow.web.example.shared;

import com.goodow.web.core.shared.ContentService;

public interface LibraryService extends ContentService<Library> {

  Library save(Library library, final boolean flag, final int size);

}
