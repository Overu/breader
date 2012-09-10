package com.goodow.web.reader.client.editgrid;

import com.goodow.web.core.client.FlowView;
import com.goodow.web.reader.client.PopupContainer;
import com.goodow.web.reader.client.PopupContainer.Loction;
import com.goodow.web.reader.client.droppable.AbstractDraggableEvent;
import com.goodow.web.reader.client.droppable.CellListDrag;
import com.goodow.web.reader.client.droppable.DroppableOptions;
import com.goodow.web.reader.client.editgrid.EditGridCell.Layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;

public class EditGridPanel extends FlowView {

  interface Bundle extends ClientBundle {

    @Source("editgridpanel.css")
    Style editGridPanelcss();

  }

  interface Style extends CssResource {

  }

  private static Bundle bundle;

  static {
    bundle = GWT.create(Bundle.class);
    bundle.editGridPanelcss().ensureInjected();
  }

  @Inject
  EditGridManager editGridManager;

  @Inject
  PopupContainer popupContainer;

  @Inject
  PresplitPanel presplitPanel;

  @Inject
  private CellListDrag cellListDrag;

  private EditGridCarousel carousel;

  private HTMLPanel buttons;
  private EditGridButton addPage;
  private EditGridButton deletePage;
  private EditGridButton savePage;
  private EditGridButton okstackPage;

  @Override
  protected void start() {
    carousel = new EditGridCarousel();
    carousel.setHeight(EditGridCell.TOP_HEIGHT + Unit.PX.getType());
    carousel.setWidth(EditGridCell.TOP_WEITH + Unit.PX.getType());
    EditGridCell stackCoverCell = editGridManager.initializeTopEditGirdCell(new Layout(0, 0));
    EditGridCell refListCell = editGridManager.initializeTopEditGirdCell(new Layout(0, 0));
    carousel.setStackCover(stackCoverCell);
    carousel.setRefList(refListCell);
    editGridManager.presentInitializeCell(stackCoverCell);
    editGridManager.presentInitializeCell(refListCell);

    buttons = new HTMLPanel("");
    buttons.getElement().getStyle().setFloat(Float.RIGHT);
    addPage = new AddPageButton();
    addPage.addDomHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        popupContainer.show(addPage, presplitPanel, Loction.OVER);
      }
    }, ClickEvent.getType());
    presplitPanel.addSelectionHandler(new SelectionHandler<Integer>() {

      @Override
      public void onSelection(final SelectionEvent<Integer> event) {
        Integer selectedItem = event.getSelectedItem();
        switch (selectedItem) {
          case 0:
            EditGridCell top0 = editGridManager.presplit1();
            carousel.addAndGoCurrentPage(top0);
            break;
          case 1:
            EditGridCell top1 = editGridManager.presplit2();
            carousel.addAndGoCurrentPage(top1);
            break;
          case 2:
            EditGridCell top2 = editGridManager.presplit3();
            carousel.addAndGoCurrentPage(top2);
            break;
          case 3:
            EditGridCell top3 = editGridManager.initializeTopEditGirdCell();
            carousel.addAndGoCurrentPage(top3);
            break;

          default:
            break;
        }
      }
    });

    deletePage = new DeletePageButton();
    deletePage.addDomHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        EditGridCell currentCell = (EditGridCell) carousel.getCurrentWidget();
        if (!carousel.remove(currentCell)) {
          Window.alert("不能删除");
          return;
        }
        editGridManager.removeTopCell(currentCell);
        currentCell = null;
      }
    }, ClickEvent.getType());

    savePage = new SaveButton();
    savePage.addDomHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
      }
    }, ClickEvent.getType());

    okstackPage = new OkstackButton();
    okstackPage.addDomHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
      }
    }, ClickEvent.getType());

    buttons.add(addPage);
    buttons.add(deletePage);
    buttons.add(savePage);
    buttons.add(okstackPage);

    cellListDrag.getElement().getStyle().setMarginTop(50, Unit.PX);
    cellListDrag.setFunction(new Function() {
      @Override
      public boolean f(final AbstractDraggableEvent event) {
        cellListDrag.setScope(DroppableOptions.DEFAULT_SCOPE
            + carousel.getCurrentWidget().getElement().hashCode());
        return true;
      }
    });
    main.add(carousel);
    main.add(buttons);
    main.add(cellListDrag);
  }
}
