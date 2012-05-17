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
package com.goodow.wave.test.domain;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.finder.Finder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

public class DemoService {
  private final Provider<EntityManager> em;

  @Inject
  DemoService(final Provider<EntityManager> em) {
    this.em = em;
  }

  @Finder(query = "select d from DemoDomain d", returnAs = ArrayList.class)
  @Transactional
  public List<DemoDomain> findAll() {
    throw new AssertionError();
  }

  public void put(final DemoDomain domain) {
    if (domain.getId() == null) {
      em.get().persist(domain);
    } else {
      em.get().merge(domain);
    }
  }
}
