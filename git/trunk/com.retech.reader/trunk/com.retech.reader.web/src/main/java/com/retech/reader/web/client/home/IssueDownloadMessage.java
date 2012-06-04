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
package com.retech.reader.web.client.home;

import com.goodow.wave.client.widget.progress.ProgressWidget;

import com.retech.reader.web.shared.proxy.IssueProxy;

public class IssueDownloadMessage {

  private IssueProxy issueProxy;

  private ProgressWidget progress;

  private int nowPage;

  private int pageCount;

  public IssueProxy getIssueProxy() {
    return issueProxy;
  }

  public int getNowPage() {
    return nowPage;
  }

  public int getPageCount() {
    return pageCount;
  }

  public ProgressWidget getProgress() {
    return progress;
  }

  public void setIssueProxy(final IssueProxy issueProxy) {
    this.issueProxy = issueProxy;
  }

  public void setNowPage(final int nowPage) {
    this.nowPage = nowPage;
  }

  public void setPageCount(final int pageCount) {
    this.pageCount = pageCount;
  }

  public void setProgress(final ProgressWidget progress) {
    this.progress = progress;
  }

}
