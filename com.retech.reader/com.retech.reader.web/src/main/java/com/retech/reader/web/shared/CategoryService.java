package com.retech.reader.web.shared;

import com.goodow.web.security.shared.ContentService;

public interface CategoryService extends ContentService<Category> {

  Long count(final Category category);

  Category findCategoryByIssue(final Issue issue);

}
