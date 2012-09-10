package com.goodow.web.reader.client.editgrid;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.reader.client.droppable.Droppable;
import com.goodow.web.reader.client.droppable.DroppableOptions;
import com.goodow.web.reader.client.droppable.SimpleQuery;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.SimpleEventBus;
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
import com.google.web.bindery.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.List;

public class EditGridCell extends Composite {

  interface Binder extends UiBinder<Widget, EditGridCell> {
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

  interface MouseHandle {

    public void addMouseDown(EditGridCell cell, Event event);

    public void addMouseMove(EditGridCell cell, Event event);

    public void addMouseOut(EditGridCell cell, Event event);

    public void addMouseOver(EditGridCell cell, Event event);

    public void addMouseUp(EditGridCell cell, Event event);

  }

  interface MyStyle extends CssResource {
    String cellhover();

    String separatorHorizontal();

    String separatorVertical();

    String splitctrls();
  }

  class SeparatorPanel extends FlowView {

    private boolean ishorizontal;

    private MouseHandle mouseHandle;

    public SeparatorPanel(final boolean ishorizontal) {
      this.ishorizontal = ishorizontal;
      if (ishorizontal) {
        main.removeStyleName(style.separatorVertical());
        main.addStyleName(style.separatorHorizontal());
      } else {
        main.removeStyleName(style.separatorHorizontal());
        main.addStyleName(style.separatorVertical());
      }
      sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEMOVE | Event.ONMOUSEOUT | Event.ONMOUSEOVER
          | Event.ONMOUSEUP);
    }

    public MouseHandle getMouseHandle() {
      return mouseHandle;
    }

    public boolean isIshorizontal() {
      return ishorizontal;
    }

    @Override
    public void onBrowserEvent(final Event event) {
      if (getMouseHandle() == null) {
        return;
      }
      int eventBus = DOM.eventGetType(event);
      switch (eventBus) {
        case Event.ONMOUSEDOWN:
          getMouseHandle().addMouseDown(EditGridCell.this, event);
          break;
        case Event.ONMOUSEOVER:
          getMouseHandle().addMouseOver(EditGridCell.this, event);
          break;
        case Event.ONMOUSEOUT:
          getMouseHandle().addMouseOut(EditGridCell.this, event);
          break;
        case Event.ONMOUSEMOVE:
          getMouseHandle().addMouseMove(EditGridCell.this, event);
          break;
        case Event.ONMOUSEUP:
          getMouseHandle().addMouseUp(EditGridCell.this, event);
          break;

        default:
          break;
      }
    }

    public void setIshorizontal(final boolean ishorizontal) {
      this.ishorizontal = ishorizontal;
    }

    public void setMouseHandle(final MouseHandle mouseHandle) {
      this.mouseHandle = mouseHandle;
    }

    @Override
    protected void start() {
    }
  }

  class SplitCtrlsItem extends FlowView {

    private Element hsplitDiv;
    private Element vsplitDiv;
    private Element removecellDiv;

    public SplitCtrlsItem() {
      main.addStyleName(style.splitctrls());
      hsplitDiv = DOM.createDiv();
      vsplitDiv = DOM.createDiv();
      removecellDiv = DOM.createDiv();
      main.getElement().appendChild(hsplitDiv);
      main.getElement().appendChild(vsplitDiv);
      main.getElement().appendChild(removecellDiv);
    }

    public void addHsplitDivHandle(final Function f) {
      addHandle(hsplitDiv, f);
    }

    public void addRemovecellDivHandle(final Function f) {
      addHandle(removecellDiv, f);
    }

    public void addVsplitDivHandle(final Function f) {
      addHandle(vsplitDiv, f);
    }

    @Override
    protected void start() {
    }

    private void addHandle(final Element elm, final Function f) {
      DOM.sinkEvents(elm, Event.ONCLICK);
      DOM.setEventListener(elm, new EventListener() {

        @Override
        public void onBrowserEvent(final Event event) {
          if (DOM.eventGetType(event) == Event.ONCLICK) {
            if (f == null) {
              return;
            }
            f.f(EditGridCell.this, event);
          }
        }
      });
    }

  }

  private final static String CELLDROPWIDGET = "__droppableWidget";
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
  private EditGridContent editGridContent;

  private Layout layout;

  private DroppableOptions droppableOptions;
  private EventBus dropHandlerManager;

  public EditGridCell() {
    this(new Layout());
  }

  public EditGridCell(final Layout layout) {
    this(layout, new DroppableOptions());
  }

  public EditGridCell(final Layout layout, final DroppableOptions droppableOptions) {
    initWidget(binder.createAndBindUi(this));
    contentCtrlsItem = new ContentCtrlsItem();
    splitCtrlsItem = new SplitCtrlsItem();
    childsGridCell = new ArrayList<EditGridCell>();
    fullOfContent(false);
    setHover(true);
    setLayout(layout);
    setDroppableOptions(droppableOptions);
    bindDropHandle();
  }

  public void addAndInsteadEditGridContent(final EditGridContent editGridContent) {
    addAndInsteadEditGridContent(editGridContent.getCaption(), editGridContent.getDes(),
        editGridContent.getSnippet());
  }

  public void addAndInsteadEditGridContent(final String caption, final String des,
      final String snippet) {
    EditGridContent content = getEditGridContent();
    content.setCaption(caption);
    content.setDes(des);
    content.setSnippet(snippet);
    if (cellMain.getWidgetIndex(editGridContent) != -1) {
      return;
    }
    fullOfContent(true);
    cellMain.add(editGridContent);
  }

  public void addChildCell(final EditGridCell childCell) {
    int widgetCount = cellMain.getWidgetCount();
    if (widgetCount == 2) {
      setSeparatorPanel(new SeparatorPanel(layout.horizontal == 1 ? true : false));
      cellMain.add(getSeparatorPanel());
      destory();
    }
    cellMain.add(childCell);
  }

  public <H extends EventHandler> HandlerRegistration addDropHandle(final H handler,
      final Type<H> type) {
    return ensureDropHandlers().addHandler(type, handler);
  }

  public void bindDropHandle() {
    SimpleQuery.q(getElement()).as(Droppable.Droppable).droppable(getDroppableOptions(),
        ensureDropHandlers()).data(CELLDROPWIDGET, this);
  }

  public void clearEditGridContent() {
    if (editGridContent == null || cellMain.getWidgetIndex(editGridContent) == -1) {
      return;
    }
    cellMain.remove(editGridContent);
    fullOfContent(false);
    editGridContent = null;
  }

  public void destory() {
    SimpleQuery.q(getElement()).as(Droppable.Droppable).destroy().removeData(CELLDROPWIDGET);
  }

  public List<EditGridCell> getChildsGridCell() {
    return childsGridCell;
  }

  public ContentCtrlsItem getContentCtrlsItem() {
    return contentCtrlsItem;
  }

  public DroppableOptions getDroppableOptions() {
    return droppableOptions;
  }

  public EditGridContent getEditGridContent() {
    if (editGridContent == null) {
      editGridContent = new EditGridContent();
    }
    return editGridContent;
  }

  public Layout getLayout() {
    return layout;
  }

  public EditGridCell getParentGridCell() {
    return parentGridCell;
  }

  public SeparatorPanel getSeparatorPanel() {
    return separatorPanel;
  }

  public SplitCtrlsItem getSplitCtrlsItem() {
    return splitCtrlsItem;
  }

  public EditGridCell getTopGridCell() {
    return topGridCell;
  }

  public boolean isCotent() {
    return cellMain.getWidgetCount() == 2 ? true : false;
  }

  public boolean remove(final EditGridCell childCell) {
    childCell.destory();
    return cellMain.remove(childCell);
  }

  public boolean removeSeparatorPanel() {
    boolean remove = cellMain.remove(getSeparatorPanel());
    setSeparatorPanel(null);
    bindDropHandle();
    return remove;
  }

  public void setDroppableOptions(final DroppableOptions droppableOptions) {
    this.droppableOptions = droppableOptions;
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

  public void setScope(final String scope) {
    SimpleQuery.q(getElement()).as(Droppable.Droppable).changeScope(droppableOptions.getScope(),
        scope);
    droppableOptions.setScope(scope);
  }

  public void setTopGridCell(final EditGridCell topGridCell) {
    this.topGridCell = topGridCell;
  }

  protected EventBus ensureDropHandlers() {
    return dropHandlerManager == null ? dropHandlerManager = new SimpleEventBus()
        : dropHandlerManager;
  }

  private void fullOfContent(final boolean isContent) {
    if (cellMain.getWidgetCount() == 0 && !isContent) {
      cellMain.add(splitCtrlsItem);
      // cellMain.add(contentCtrlsItem);
      return;
    }

    cellMain.remove(0);
    if (isContent) {
      cellMain.insert(contentCtrlsItem, 0);
    } else {
      cellMain.insert(splitCtrlsItem, 0);
    }
  }

  private void setSeparatorPanel(final SeparatorPanel separatorPanel) {
    this.separatorPanel = separatorPanel;
  }
}
