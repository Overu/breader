package org.cloudlet.web.ui.client.nav;

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
  public void render(com.google.gwt.cell.client.Cell.Context context, TreeNodeProxy value,
      SafeHtmlBuilder sb) {
    if (value != null) {
      sb.appendEscaped(value.getName() == null ? value.getPath() : value.getName());
    }
  }
}