package com.goodow.web.reader.client.editgrid;

import com.goodow.web.core.client.FlowView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class EditGridCell extends Composite {

  interface Binder extends UiBinder<Widget, EditGridCell> {
  }

  class ContentCtrlsItem extends FlowView {

    private EditGridCell cell;

    public ContentCtrlsItem(final EditGridCell cell) {
      this.cell = cell;
    }

    @Override
    protected void start() {

    }

  }

  interface EditGridCellFunction {
    public void f(EditGridCell cell, Event event);
  }

  static class Layout {

    private int horizontal;

    private int vertical;

    private double height;

    private double weith;

    public Layout() {
      this(0, 0, 0.0, 0.0);
    }

    public Layout(final int horizontal, final int vertical) {
      this(horizontal, vertical, 0.0, 0.0);
    }

    public Layout(final int horizontal, final int vertical, final double height, final double weith) {
      if (horizontal == 1 && vertical == 1) {
        return;
      }
      this.setHorizontal(horizontal);
      this.setVertical(vertical);
      this.setHeight(height);
      this.setWeith(weith);
    }

    public double getHeight() {
      return height;
    }

    public int getHorizontal() {
      return horizontal;
    }

    public int getVertical() {
      return vertical;
    }

    public double getWeith() {
      return weith;
    }

    public boolean isSplit() {
      return horizontal >= 1 || vertical >= 1 ? true : false;
    }

    public void setHeight(final double height) {
      this.height = height;
    }

    public void setHorizontal(final int horizontal) {
      this.horizontal = horizontal;
    }

    public void setVertical(final int vertical) {
      this.vertical = vertical;
    }

    public void setWeith(final double weith) {
      this.weith = weith;
    }

  }

  interface MyStyle extends CssResource {
    String cellhover();

    String separatorHorizontal();

    String separatorVertical();

    String splitctrls();
  }

  class SeparatorPanel extends FlowView {

    private boolean ishorizontal;

    public SeparatorPanel(final boolean ishorizontal) {
      this.ishorizontal = ishorizontal;
      if (ishorizontal) {
        main.removeStyleName(style.separatorVertical());
        main.addStyleName(style.separatorHorizontal());
      } else {
        main.removeStyleName(style.separatorHorizontal());
        main.addStyleName(style.separatorVertical());
      }

    }

    public boolean isIshorizontal() {
      return ishorizontal;
    }

    public void setIshorizontal(final boolean ishorizontal) {
      this.ishorizontal = ishorizontal;
    }

    @Override
    protected void start() {

    }

  }

  class SplitCtrlsItem extends FlowView {

    private Element hsplitDiv;
    private Element vsplitDiv;
    private Element removecellDiv;

    private EditGridCell cell;

    public SplitCtrlsItem(final EditGridCell cell) {
      this.cell = cell;
      main.addStyleName(style.splitctrls());
      hsplitDiv = DOM.createDiv();
      vsplitDiv = DOM.createDiv();
      removecellDiv = DOM.createDiv();
      main.getElement().appendChild(hsplitDiv);
      main.getElement().appendChild(vsplitDiv);
      main.getElement().appendChild(removecellDiv);
    }

    public void addHsplitDivHandle(final EditGridCellFunction f) {
      addHandle(hsplitDiv, f);
    }

    public void addRemovecellDivHandle(final EditGridCellFunction f) {
      addHandle(removecellDiv, f);
    }

    public void addVsplitDivHandle(final EditGridCellFunction f) {
      addHandle(vsplitDiv, f);
    }

    @Override
    protected void start() {
    }

    private void addHandle(final Element elm, final EditGridCellFunction f) {
      DOM.sinkEvents(elm, Event.ONCLICK);
      DOM.setEventListener(elm, new EventListener() {

        @Override
        public void onBrowserEvent(final Event event) {
          if (DOM.eventGetType(event) == Event.ONCLICK) {
            if (f == null) {
              return;
            }
            f.f(cell, event);
          }
        }
      });
    }

  }

  private static Binder binder = GWT.create(Binder.class);

  public static final double SEPARATOR_SIZE = 4.0;
  public static final String TOP_CELL = "top-cell";
  public static final double TOP_WEITH = 745.0;
  public static final double TOP_HEIGHT = 560.0;
  public static final int CELL_MIX_WEITH_HEIGHT = 181;
  public static final int CELL_MIX_SPLIT_WEITH_HEIGHT = CELL_MIX_WEITH_HEIGHT * 2;

  @UiField
  FlowPanel cellMain;
  @UiField
  MyStyle style;

  private EditGridCell parentGridCell;
  private EditGridCell topGridCell;
  private List<EditGridCell> childsGridCell;

  private ContentCtrlsItem contentCtrlsItem;
  private SplitCtrlsItem splitCtrlsItem;
  private SeparatorPanel separatorPanel;

  private Layout layout;

  private boolean isContent = false;

  public EditGridCell() {
    this(new Layout());
  }

  public EditGridCell(final Layout layout) {
    initWidget(binder.createAndBindUi(this));
    contentCtrlsItem = new ContentCtrlsItem(this);
    splitCtrlsItem = new SplitCtrlsItem(this);
    childsGridCell = new ArrayList<EditGridCell>();
    fullOfContent();
    setHover(true);
    setLayout(layout);
  }

  public void addChildCell(final EditGridCell childCell) {
    int widgetCount = cellMain.getWidgetCount();
    if (widgetCount == 2) {
      separatorPanel = new SeparatorPanel(layout.horizontal == 1 ? true : false);
      cellMain.add(separatorPanel);
    }
    cellMain.add(childCell);
  }

  public List<EditGridCell> getChildsGridCell() {
    return childsGridCell;
  }

  public ContentCtrlsItem getContentCtrlsItem() {
    return contentCtrlsItem;
  }

  public Layout getLayout() {
    return layout;
  }

  public EditGridCell getParentGridCell() {
    return parentGridCell;
  }

  public SplitCtrlsItem getSplitCtrlsItem() {
    return splitCtrlsItem;
  }

  public EditGridCell getTopGridCell() {
    return topGridCell;
  }

  public boolean remove(final EditGridCell childCell) {
    return cellMain.remove(childCell);
  }

  public boolean removeSeparatorPanel() {
    boolean remove = cellMain.remove(separatorPanel);
    separatorPanel = null;
    return remove;
  }

  public void setHover(final boolean isHover) {
    if (isHover) {
      cellMain.addStyleName(style.cellhover());
    } else {
      cellMain.removeStyleName(style.cellhover());
    }
  }

  public void setLayout(final Layout layout) {
    this.layout = layout;
  }

  public void setParentGridCell(final EditGridCell parentGridCell) {
    this.parentGridCell = parentGridCell;
  }

  public void setTopGridCell(final EditGridCell topGridCell) {
    this.topGridCell = topGridCell;
  }

  private void fullOfContent() {
    if (cellMain.getWidgetCount() == 0 && !isContent) {
      cellMain.add(splitCtrlsItem);
      return;
    }

    cellMain.remove(0);
    if (isContent) {
      cellMain.insert(contentCtrlsItem, 0);
      isContent = true;
    } else {
      cellMain.insert(splitCtrlsItem, 0);
      isContent = false;
    }
  }
}
