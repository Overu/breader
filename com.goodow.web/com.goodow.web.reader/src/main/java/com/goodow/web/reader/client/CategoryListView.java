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

import com.goodow.web.reader.client.style.ReadResources;

import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedHandler;

import java.util.ArrayList;
import java.util.List;

public class CategoryListView extends ScrollPanel {

  CellList<Category> categorList;
  CategoryCell cell = new CategoryCell();
  private int oldIndex;

  public CategoryListView() {
    categorList = new CellList<Category>(cell, ReadResources.INSTANCE().categroyListCss());
    categorList.render(createList());
    setScrollingEnabledX(false);
    setScrollingEnabledY(true);
    setWidget(categorList);

    categorList.addCellSelectedHandler(new CellSelectedHandler() {

      @Override
      public void onCellSelected(final CellSelectedEvent event) {
        int index = event.getIndex();
        categorList.setSelectedIndex(oldIndex, false);
        categorList.setSelectedIndex(index, true);
        oldIndex = index;
      }
    });
  }

  private List<Category> createList() {
    List<Category> categorys = new ArrayList<Category>();
    categorys.add(new Category("小说", 81));
    categorys.add(new Category("两性情感", 11));
    categorys.add(new Category("经营", 16));
    categorys.add(new Category("计算机", 6));
    categorys.add(new Category("文学", 43));
    categorys.add(new Category("生活", 12));
    categorys.add(new Category("传记", 8));
    categorys.add(new Category("旅游", 3));
    categorys.add(new Category("成功励志", 7));
    categorys.add(new Category("历史", 20));
    categorys.add(new Category("艺术", 1));
    categorys.add(new Category("社会科学", 10));
    categorys.add(new Category("杂志", 3));
    categorys.add(new Category("外语", 1));
    return categorys;
  }
}
