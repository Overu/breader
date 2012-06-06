package com.goodow.web.example.shared;

import com.goodow.web.core.shared.Service;
import com.goodow.web.core.shared.WebService;

@WebService
public interface LibraryService extends Service {

  Library save(Library library, final boolean flag, final int size);

}
