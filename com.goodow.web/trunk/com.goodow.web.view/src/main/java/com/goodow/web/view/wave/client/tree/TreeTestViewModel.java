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
package com.goodow.web.view.wave.client.tree;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

import java.util.ArrayList;
import java.util.List;

public class TreeTestViewModel implements TreeViewModel {

  // private Map<String, List<String>> title;

  private ListDataProvider<String> titles = new ListDataProvider<String>();
  private CompositeCell<Integer> composite;

  private AbstractCell<String> cell = new AbstractCell<String>() {

    @Override
    public void render(final com.google.gwt.cell.client.Cell.Context context, final String value,
        final SafeHtmlBuilder sb) {
      sb.append(SafeHtmlUtils.fromString(value));
    }

  };

  TreeTestViewModel() {
    List<String> title = titles.getList();
    title.add("a");
    title.add("b");
    title.add("c");
    title.add("d");

    List<HasCell<Integer, ?>> hasCell = new ArrayList<HasCell<Integer, ?>>();
    hasCell.add(new HasCell<Integer, Integer>() {

      private AbstractCell<Integer> cell = new AbstractCell<Integer>() {

        @Override
        public void render(final com.google.gwt.cell.client.Cell.Context context,
            final Integer value, final SafeHtmlBuilder sb) {
          sb.append(value.intValue());
        }
      };

      @Override
      public Cell<Integer> getCell() {
        return cell;
      }

      @Override
      public FieldUpdater<Integer, Integer> getFieldUpdater() {
        return null;
      }

      @Override
      public Integer getValue(final Integer object) {
        return object;
      }
    });

    hasCell.add(new HasCell<Integer, Integer>() {

      private TrangleButtonCell<Integer> tbc = new TrangleButtonCell<Integer>();

      @Override
      public Cell<Integer> getCell() {
        return tbc;
      }

      @Override
      public FieldUpdater<Integer, Integer> getFieldUpdater() {
        return null;
      }

      @Override
      public Integer getValue(final Integer object) {
        return object;
      }
    });

    composite = new CompositeCell<Integer>(hasCell) {

      @Override
      public void render(final Context context, final Integer value, final SafeHtmlBuilder sb) {
        super.render(context, value, sb);
      }

      @Override
      protected Element getContainerElement(final Element parent) {
        return parent;
      }

      @Override
      protected <X> void render(final Context context, final Integer value,
          final SafeHtmlBuilder sb, final HasCell<Integer, X> hasCell) {
        Cell<X> cell = hasCell.getCell();
        sb.append(SafeHtmlUtils.fromTrustedString("<div>"));
        cell.render(context, hasCell.getValue(value), sb);
        sb.append(SafeHtmlUtils.fromTrustedString("</div>"));
      }
    };

  }

  @Override
  public <T> NodeInfo<?> getNodeInfo(final T value) {
    if (value == null) {
      return new DefaultNodeInfo<String>(titles, cell);
    } else if (value instanceof String) {
      List<Integer> a = new ArrayList<Integer>();
      a.add(new Integer(50));
      a.add(new Integer(50));
      return new DefaultNodeInfo<Integer>(new ListDataProvider<Integer>(a), composite);
    }
    String type = value.getClass().getName();
    throw new IllegalArgumentException("Unsupported object type: " + type);
  }

  @Override
  public boolean isLeaf(final Object value) {
    return value instanceof Integer;
  }

}
