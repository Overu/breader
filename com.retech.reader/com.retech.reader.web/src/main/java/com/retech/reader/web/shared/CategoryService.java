package com.retech.reader.web.shared;

import com.goodow.web.mvp.shared.BaseService;

public interface CategoryService extends BaseService<Category> {

  Long count(final Category category);

  Category findCategoryByIssue(final Issue issue);

}
