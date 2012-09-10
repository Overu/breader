package com.goodow.web.reader.client.editgrid;

import com.goodow.web.reader.client.droppable.DropEvent;
import com.goodow.web.reader.client.droppable.DroppableOptions;
import com.goodow.web.reader.client.editgrid.EditGridCell.Layout;
import com.goodow.web.reader.client.editgrid.EditGridCell.MouseHandle;
import com.goodow.web.reader.client.editgrid.EditGridCell.SeparatorPanel;
import com.goodow.web.reader.client.editgrid.EditGridCell.SplitCtrlsItem;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class EditGridManager {

  private boolean separatorDragging = false;
  private int separatorDraggingX;
  private int separatorDraggingY;

  private String pct = Unit.PCT.getType();

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

  public EditGridCell initializeEditGirdCell(final EditGridCell parent,
      final DroppableOptions options) {
    return initializeEditGirdCell(parent, new Layout(), options);
  }

  public EditGridCell initializeEditGirdCell(final EditGridCell parent, final Layout layout) {
    return initializeEditGirdCell(parent, layout, new DroppableOptions());
  }

  public EditGridCell initializeEditGirdCell(final EditGridCell parent, final Layout layout,
      final DroppableOptions options) {
    EditGridCell cell = new EditGridCell(layout, options);

    if (parent != null) {
      cell.setTopGridCell(parent.getTopGridCell());
      cell.setParentGridCell(parent);
      parent.setHover(false);
    }
    initializeCellDropHandle(cell);
    initializeSplitCtrlsItemHandle(cell);
    initializeCellContentHandle(cell);
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
      cell.setScope(cell.getDroppableOptions().getScope() + cell.getElement().hashCode());
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

  private void beginDragging(final SeparatorPanel sPanel, final Event event) {
    if (DOM.getCaptureElement() == null) {
      separatorDragging = true;
      Element target = sPanel.getElement();
      DOM.setCapture(target);
      separatorDraggingX = getX(target, event);
      separatorDraggingY = getY(target, event);
    }
  }

  private void calculateHeightOrWidth(final EditGridCell cell, final boolean isHeight) {
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

  private void changeContentLoction(final EditGridCell cell) {
    EditGridContent editGridContent = cell.getEditGridContent();
    cell.clearEditGridContent();
    calculateLayout(cell, false);
    cell.getChildsGridCell().get(0).addAndInsteadEditGridContent(editGridContent);
  }

  private void clearLayout(final Layout layout) {
    layout.setHorizontal(0);
    layout.setVertical(0);
  }

  private void endDragging(final SeparatorPanel sPanel) {
    separatorDragging = false;
    DOM.releaseCapture(sPanel.getElement());
  }

  private void generateChildCell(final EditGridCell parentCell) {
    List<EditGridCell> childsGridCell = parentCell.getChildsGridCell();
    for (int i = 0; i < 2; i++) {
      EditGridCell childCell =
          initializeEditGirdCell(parentCell, parentCell.getTopGridCell().getDroppableOptions());
      childsGridCell.add(childCell);
      parentCell.addChildCell(childCell);
    }
  }

  private int getX(final Element target, final Event event) {
    return event.getClientX() - target.getAbsoluteLeft() + target.getScrollLeft()
        + target.getOwnerDocument().getScrollLeft();
  }

  private int getY(final Element target, final Event event) {
    return event.getClientY() - target.getAbsoluteTop() + target.getScrollTop()
        + target.getOwnerDocument().getScrollTop();
  }

  private boolean hsplitBaseHandle(final EditGridCell cell, final boolean isContent) {
    if (cell.getOffsetHeight() < EditGridCell.CELL_MIX_SPLIT_WEITH_HEIGHT) {
      Window.alert("高度不能分割");
      return false;
    }
    setCellVertical(cell, true);
    if (isContent) {
      changeContentLoction(cell);
      return true;
    }
    calculateLayout(cell, false);
    return true;
  }

  private void initializeCellContentHandle(final EditGridCell cell) {
    ContentCtrlsItem contentCtrlsItem = cell.getContentCtrlsItem();
    contentCtrlsItem.addClearElmHandle(new Function() {

      @Override
      public boolean f(final Event event) {
        cell.clearEditGridContent();
        return true;
      }
    });

    contentCtrlsItem.addHsplitElmHandle(new Function() {

      @Override
      public boolean f(final Event event) {
        hsplitBaseHandle(cell, true);
        return true;
      }
    });

    contentCtrlsItem.addVsplitElmHandle(new Function() {

      @Override
      public boolean f(final Event event) {
        vsplitBaseHandle(cell, true);
        return true;
      }

    });

  }

  private void initializeCellDropHandle(final EditGridCell cell) {
    cell.addDropHandle(new DropEvent.DropEventHandler() {

      @Override
      public void onDrop(final DropEvent event) {
        Book book = (Book) event.getDraggableData();
        cell.addAndInsteadEditGridContent(book.getTitle(), book.getDescription(), book
            .getDescription());
      }
    }, DropEvent.TYPE);
  }

  private void initializeSeparatorPanelMouseHandle(final EditGridCell cell) {
    final SeparatorPanel separatorPanel = cell.getSeparatorPanel();
    if (separatorPanel.getMouseHandle() != null) {
      return;
    }
    separatorPanel.setMouseHandle(new MouseHandle() {

      @Override
      public void addMouseDown(final EditGridCell cell, final Event event) {
        event.stopPropagation();
        beginDragging(separatorPanel, event);
      }

      @Override
      public void addMouseMove(final EditGridCell cell, final Event event) {
        moveDragging(cell, separatorPanel, event);
      }

      @Override
      public void addMouseOut(final EditGridCell cell, final Event event) {
      }

      @Override
      public void addMouseOver(final EditGridCell cell, final Event event) {
      }

      @Override
      public void addMouseUp(final EditGridCell cell, final Event event) {
        endDragging(separatorPanel);
      }
    });
  }

  private void initializeSplitCtrlsItemHandle(final EditGridCell cell) {
    SplitCtrlsItem splitCtrlsItem = cell.getSplitCtrlsItem();

    splitCtrlsItem.addHsplitDivHandle(new Function() {

      @Override
      public boolean f(final EditGridCell cell, final Event event) {
        return !hsplitBaseHandle(cell, false);
      }
    });

    splitCtrlsItem.addVsplitDivHandle(new Function() {

      @Override
      public boolean f(final EditGridCell cell, final Event event) {
        return !vsplitBaseHandle(cell, false);
      }
    });

    splitCtrlsItem.addRemovecellDivHandle(new Function() {

      @Override
      public boolean f(final EditGridCell cell, final Event event) {
        if (cell.getElement().getAttribute(EditGridCell.TOP_CELL).equals(EditGridCell.TOP_CELL)) {
          Window.alert("顶层不能删除");
          return false;
        }
        EditGridCell parentGridCell = cell.getParentGridCell();
        removeChildCell(parentGridCell);
        return false;
      }
    });
  }

  private boolean ischildCell(final EditGridCell childCell) {
    if (childCell.getChildsGridCell().size() == 0) {
      return false;
    }
    return true;
  }

  private void moveDragging(final EditGridCell cell, final SeparatorPanel sPanel, final Event event) {
    Element target = sPanel.getElement();
    if (separatorDragging) {
      double cellHeight = cell.getOffsetHeight();
      double cellChildsHeight = cellHeight - EditGridCell.SEPARATOR_SIZE;
      double cellWidth = cell.getOffsetWidth();
      double cellChildsWidth = cellWidth - EditGridCell.SEPARATOR_SIZE;
      EditGridCell child1 = cell.getChildsGridCell().get(0);
      EditGridCell child2 = cell.getChildsGridCell().get(1);
      if (sPanel.isIshorizontal()) {
        if (child1.getOffsetHeight() < EditGridCell.CELL_MIX_WEITH_HEIGHT) {
          setCellHeight(child1, child2, cellChildsHeight, EditGridCell.CELL_MIX_WEITH_HEIGHT,
              cellHeight);
          endDragging(sPanel);
          return;
        }
        if (child2.getOffsetHeight() < EditGridCell.CELL_MIX_WEITH_HEIGHT) {
          setCellHeight(child2, child1, cellChildsHeight, EditGridCell.CELL_MIX_WEITH_HEIGHT,
              cellHeight);
          endDragging(sPanel);
          return;
        }
        double child1Height = child1.getOffsetHeight();
        double absChild1Height = child1Height + getY(target, event) - separatorDraggingY;
        setCellHeight(child1, child2, cellChildsHeight, absChild1Height, cellHeight);
      } else {
        if (child1.getOffsetWidth() < EditGridCell.CELL_MIX_WEITH_HEIGHT) {
          setCellWidth(child1, child2, cellChildsWidth, EditGridCell.CELL_MIX_WEITH_HEIGHT,
              cellWidth);
          endDragging(sPanel);
          return;
        }
        if (child2.getOffsetWidth() < EditGridCell.CELL_MIX_WEITH_HEIGHT) {
          setCellWidth(child2, child1, cellChildsWidth, EditGridCell.CELL_MIX_WEITH_HEIGHT,
              cellWidth);
          endDragging(sPanel);
          return;
        }
        double child1Width = child1.getOffsetWidth();
        double absChild1Width = child1Width + getX(target, event) - separatorDraggingX;
        setCellWidth(child1, child2, cellChildsWidth, absChild1Width, cellWidth);
      }
    }
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
    initializeSeparatorPanelMouseHandle(parentCell);
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

  private void setCellHeight(final EditGridCell child1, final EditGridCell child2,
      final double cellChildsHeight, final double absChild1Height, final double cellHeight) {
    double absChild1Pct = absChild1Height / cellHeight;
    double absChile2Pct = (cellChildsHeight - absChild1Height) / cellHeight;
    child1.setHeight(absChild1Pct * 100 + pct);
    child2.setHeight(absChile2Pct * 100 + pct);
    child1.getLayout().setHeight(absChild1Pct);
    child2.getLayout().setHeight(absChile2Pct);
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

  private void setCellWidth(final EditGridCell child1, final EditGridCell child2,
      final double cellChildsWidth, final double absChild1Width, final double cellWidth) {
    double absChild1Pct = absChild1Width / cellWidth;
    double absChile2Pct = (cellChildsWidth - absChild1Width) / cellWidth;
    child1.setWidth(absChild1Pct * 100 + pct);
    child2.setWidth(absChile2Pct * 100 + pct);
    child1.getLayout().setWeith(absChild1Pct);
    child2.getLayout().setWeith(absChile2Pct);
  }

  private boolean vsplitBaseHandle(final EditGridCell cell, final boolean isContent) {
    if (cell.getOffsetWidth() < EditGridCell.CELL_MIX_SPLIT_WEITH_HEIGHT) {
      Window.alert("宽度不能分割");
      return false;
    }
    setCellVertical(cell, false);
    if (isContent) {
      changeContentLoction(cell);
      return true;
    }
    calculateLayout(cell, false);
    return true;
  }
}
