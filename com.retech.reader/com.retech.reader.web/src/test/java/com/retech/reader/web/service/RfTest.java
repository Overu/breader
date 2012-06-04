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
package com.retech.reader.web.service;

import com.goodow.wave.test.BaseRfTest;

import com.google.web.bindery.requestfactory.shared.Receiver;

import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.junit.Test;

import java.util.List;

public class RfTest extends BaseRfTest<ReaderFactory> {
  @Test
  public void test() {
    f.issue().findHelpIssue(7).fire(new Receiver<List<IssueProxy>>() {

      @Override
      public void onSuccess(final List<IssueProxy> response) {
        // TODO Auto-generated method stub

      }

    });
  }
}
