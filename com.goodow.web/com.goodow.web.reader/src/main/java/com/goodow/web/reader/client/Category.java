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
package com.goodow.web.reader.client;

public class Category {

  private String name;

  private int bookCount;

  public Category(final String name, final int bookCount) {
    this.name = name;
    this.bookCount = bookCount;
  }

  /**
   * @return the bookCount
   */
  public int getBookCount() {
    return bookCount;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param bookCount the bookCount to set
   */
  public void setBookCount(final int bookCount) {
    this.bookCount = bookCount;
  }

  /**
   * @param name the name to set
   */
  public void setName(final String name) {
    this.name = name;
  }
}
