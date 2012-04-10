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
package com.goodow.web.ui.client.nav;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.inject.Singleton;

import org.cloudlet.web.mvp.shared.tree.TreeNodeProxy;

@Singleton
public class TreeNodeCell extends AbstractCell<TreeNodeProxy> {

  @Override
  public boolean dependsOnSelection() {
    return true;
  }

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context,
      final TreeNodeProxy value, final SafeHtmlBuilder sb) {
    if (value != null) {
      sb.appendEscaped(value.getName() == null ? value.getPath() : value.getName());
    }
  }
}