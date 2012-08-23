package com.goodow.web.reader.client.editgrid;

import com.goodow.web.reader.client.editgrid.EditGridCell.EditGridCellFunction;
import com.goodow.web.reader.client.editgrid.EditGridCell.Layout;
import com.goodow.web.reader.client.editgrid.EditGridCell.SplitCtrlsItem;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class EditGridManager {

  public void calculateLayout(final EditGridCell parentCell, final boolean isPresent) {
    Layout layout = parentCell.getLayout();

    int horizontal = layout.getHorizontal();
    int vertical = layout.getVertical();

    if (horizontal == 0 && vertical == 0) {
      return;
    }

    if (isPresent) {
      if (horizontal == 1) {
        presentChildCell(parentCell, 1);
      } else if (vertical == 1) {
        presentChildCell(parentCell, 2);
      }
      return;
    }

    if (horizontal == 1) {
      addChildCell(parentCell, 1);
    } else if (vertical == 1) {
      addChildCell(parentCell, 2);
    }
  }

  public EditGridCell initializeEditGirdCell() {
    return initializeEditGirdCell(null, new Layout());
  }

  public EditGridCell initializeEditGirdCell(final EditGridCell parent) {
    return initializeEditGirdCell(parent, new Layout());
  }

  public EditGridCell initializeEditGirdCell(final EditGridCell parent, final Layout layout) {
    EditGridCell cell = new EditGridCell(layout);

    if (parent != null) {
      cell.setTopGridCell(parent.getTopGridCell());
      cell.setParentGridCell(parent);
      parent.setHover(false);
    }
    initializeSplitCtrlsItemHandle(cell);
    return cell;
  }

  public EditGridCell initializeTopEditGirdCell() {
    return initializeTopEditGirdCell(new Layout());
  }

  public EditGridCell initializeTopEditGirdCell(final Layout layout) {
    final EditGridCell cell = initializeEditGirdCell(null, layout);
    if (cell.getTopGridCell() == null && cell.getParentGridCell() == null) {
      cell.setTopGridCell(cell);
      cell.getElement().setAttribute(EditGridCell.TOP_CELL, EditGridCell.TOP_CELL);
      cell.setParentGridCell(cell);
      cell.getLayout().setHeight(1.0);
      cell.getLayout().setWeith(1.0);
    }
    return cell;
  }

  public void presentExistingCell(final EditGridCell topCell) {
    calculateLayout(topCell, true);
  }

  public void presentInitializeCell(final EditGridCell topCell) {
    calculateLayout(topCell, false);
  }

  public EditGridCell presplit1() {
    EditGridCell topCell = basePresplit(new Layout(0, 1));
    topCell.getChildsGridCell().get(0).setLayout(new Layout(1, 0));
    presentExistingCell(topCell);
    return topCell;
  }

  public EditGridCell presplit2() {
    EditGridCell topCell = basePresplit(new Layout(1, 0));
    topCell.getChildsGridCell().get(0).setLayout(new Layout(0, 1));
    presentExistingCell(topCell);
    return topCell;
  }

  public EditGridCell presplit3() {
    EditGridCell topCell = basePresplit(new Layout(1, 0));
    topCell.getChildsGridCell().get(1).setLayout(new Layout(0, 1));
    presentExistingCell(topCell);
    return topCell;
  }

  public void removeTopCell(final EditGridCell topCell) {
    topCell.removeFromParent();
    removeParentCell(topCell);
  }

  private void addChildCell(final EditGridCell parentCell, final int isHorizontal) {
    generateChildCell(parentCell);
    presentChildCell(parentCell, isHorizontal);
  }

  private EditGridCell basePresplit(final Layout layou) {
    EditGridCell topCell = initializeTopEditGirdCell(layou);
    generateChildCell(topCell);
    return topCell;
  }

  private void calculateHeightOrWidth(final EditGridCell cell, final boolean isHeight) {
    String pct = Unit.PCT.getType();
    double separatorSize = EditGridCell.SEPARATOR_SIZE;
    double height = cell.getLayout().getHeight();
    double weith = cell.getLayout().getWeith();

    if (height != 0.0 || weith != 0.0) {
      if (isHeight) {
        cell.setHeight(height * 100 + pct);
      } else {
        cell.setWidth(weith * 100 + pct);
      }
      return;
    }

    if (isHeight && height == 0.0) {
      double parentHeightPct = cell.getParentGridCell().getLayout().getHeight();
      double parentHeight = EditGridCell.TOP_HEIGHT * parentHeightPct;
      double cellHeightParent = ((parentHeight - separatorSize) / parentHeight) / 2.0;
      cell.setHeight(cellHeightParent * 100 + pct);
      cell.setWidth("100" + pct);
      cell.getLayout().setHeight(cellHeightParent);
      cell.getLayout().setWeith(1.0);
    } else if (weith == 0.0) {
      double parentWidthPct = cell.getParentGridCell().getLayout().getWeith();
      double parentWidth = EditGridCell.TOP_WEITH * parentWidthPct;
      double cellWidthParcent = ((parentWidth - separatorSize) / parentWidth) / 2.0;
      cell.setWidth(cellWidthParcent * 100 + pct);
      cell.setHeight("100" + pct);
      cell.getLayout().setWeith(cellWidthParcent);
      cell.getLayout().setHeight(1.0);
    }

  }

  private void clearLayout(final Layout layout) {
    layout.setHorizontal(0);
    layout.setVertical(0);
  }

  private void generateChildCell(final EditGridCell parentCell) {
    List<EditGridCell> childsGridCell = parentCell.getChildsGridCell();
    for (int i = 0; i < 2; i++) {
      EditGridCell childCell = initializeEditGirdCell(parentCell);
      childsGridCell.add(childCell);
      parentCell.addChildCell(childCell);
    }
  }

  private void initializeSplitCtrlsItemHandle(final EditGridCell cell) {
    SplitCtrlsItem splitCtrlsItem = cell.getSplitCtrlsItem();

    splitCtrlsItem.addHsplitDivHandle(new EditGridCellFunction() {

      @Override
      public void f(final EditGridCell cell, final Event event) {
        if (cell.getOffsetHeight() < EditGridCell.CELL_MIX_SPLIT_WEITH_HEIGHT) {
          Window.alert("高度不能分割");
          return;
        }
        setCellVertical(cell, true);
        calculateLayout(cell, false);
      }
    });

    splitCtrlsItem.addVsplitDivHandle(new EditGridCellFunction() {

      @Override
      public void f(final EditGridCell cell, final Event event) {
        if (cell.getOffsetWidth() < EditGridCell.CELL_MIX_SPLIT_WEITH_HEIGHT) {
          Window.alert("宽度不能分割");
          return;
        }
        setCellVertical(cell, false);
        calculateLayout(cell, false);
      }
    });

    splitCtrlsItem.addRemovecellDivHandle(new EditGridCellFunction() {

      @Override
      public void f(final EditGridCell cell, final Event event) {
        if (cell.getElement().getAttribute(EditGridCell.TOP_CELL).equals(EditGridCell.TOP_CELL)) {
          Window.alert("顶层不能删除");
          return;
        }
        EditGridCell parentGridCell = cell.getParentGridCell();
        removeChildCell(parentGridCell);
      }
    });
  }

  private boolean ischildCell(final EditGridCell childCell) {
    if (childCell.getChildsGridCell().size() == 0) {
      return false;
    }
    return true;
  }

  private void presentChildCell(final EditGridCell parentCell, final int isHorizontal) {
    if (!ischildCell(parentCell)) {
      return;
    }
    List<EditGridCell> childsGridCells = parentCell.getChildsGridCell();

    for (EditGridCell childCell : childsGridCells) {
      switch (isHorizontal) {
        case 1:
          calculateHeightOrWidth(childCell, true);
          break;
        case 2:
          calculateHeightOrWidth(childCell, false);
          break;

        default:
          break;
      }
      if (!ischildCell(childCell)) {
        calculateLayout(childCell, false);
        continue;
      }
      calculateLayout(childCell, true);
    }
  }

  private void removeChildCell(final EditGridCell parentCell) {
    if (!ischildCell(parentCell)) {
      return;
    }
    List<EditGridCell> childsGridCell = parentCell.getChildsGridCell();

    int indexChildRemove = -1;

    for (int i = 0; i < childsGridCell.size(); i++) {
      EditGridCell childCell = childsGridCell.get(i);
      if (ischildCell(childCell)) {
        indexChildRemove = i;
      }
    }
    if (indexChildRemove != -1) {
      removeChildCell(childsGridCell.get(indexChildRemove));
      return;
    }
    for (EditGridCell childCell : childsGridCell) {
      parentCell.remove(childCell);
      childCell = null;
    }
    childsGridCell.clear();
    parentCell.removeSeparatorPanel();
    parentCell.setHover(true);
    clearLayout(parentCell.getLayout());
  }

  private void removeParentCell(final EditGridCell parentCell) {
    if (!ischildCell(parentCell)) {
      return;
    }
    List<EditGridCell> childsGridCell = parentCell.getChildsGridCell();
    for (EditGridCell childCell : childsGridCell) {
      if (ischildCell(childCell)) {
        removeParentCell(childCell);
      }
      childCell = null;
    }
  }

  private void setCellVertical(final EditGridCell cell, final boolean isHorizontal) {
    Layout layout = cell.getLayout();
    int vertical = layout.getVertical();
    int horizontal = layout.getHorizontal();
    if (horizontal != 0 && vertical != 0) {
      return;
    }

    if (isHorizontal) {
      layout.setHorizontal(1);
    } else {
      layout.setVertical(1);
    }
  }
}