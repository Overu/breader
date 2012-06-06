package com.goodow.web.example.shared;

import com.goodow.web.core.shared.WebService;
import com.goodow.web.security.shared.ContentService;

@WebService
public interface LibraryService extends ContentService<Library> {

  Library save(Library library, final boolean flag, final int size);

}
