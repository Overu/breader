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
package com.goodow.wave.client.wavepanel.blip;

import com.goodow.wave.client.wavepanel.WavePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TreeTest extends WavePanel {

  interface Resource extends CellTree.Resources {
    @Override
    @Source({CellTree.Style.DEFAULT_CSS, "WaveTree.css"})
    CellTree.Style cellTreeStyle();
  }

  private static Resource resources = GWT.create(Resource.class);
  private TreeTestViewModel treeViewModel = new TreeTestViewModel();

  @Inject
  TreeTest() {
    CellTree cellTree = new CellTree(treeViewModel, null, resources);
    this.setWaveContent(cellTree);
  }
}
