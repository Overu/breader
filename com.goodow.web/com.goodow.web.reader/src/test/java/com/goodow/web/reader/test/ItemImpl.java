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
package com.goodow.web.reader.test;

public class ItemImpl implements Item {

  private String name;

  private int price;

  /*
   * (non-Javadoc)
   * 
   * @see com.goodow.web.example.test.Item#getName()
   */
  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.goodow.web.example.test.Item#getPrice()
   */
  @Override
  public int getPrice() {
    // TODO Auto-generated method stub
    return price;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.goodow.web.example.test.Item#setName(java.lang.String)
   */
  @Override
  public void setName(final String name) {
    this.name = name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.goodow.web.example.test.Item#setPrice(int)
   */
  @Override
  public void setPrice(final int value) {
    this.price = value;
  }

}
