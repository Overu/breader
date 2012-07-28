/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.goodow.web.core.jpa;

import com.goodow.web.core.shared.Category;
import com.goodow.web.core.shared.CategoryService;

import java.util.List;

import javax.annotation.Generated;

@Generated("cloudlet")
public class JpaCategoryService extends JpaEntityService<Category> implements CategoryService {

  @Override
  public List<Category> getCategory() {
    List<Category> result =
        em().createQuery("select c from Category c", Category.class).getResultList();
    return result;
  }

}
