package com.goodow.web.reader.client;

import com.goodow.web.core.shared.AsyncSectionService;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.Section;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Singleton
public class BookEditor extends FormView<Book> {

  interface Binder extends UiBinder<Widget, BookEditor> {
  }

  private class DefaultCell extends AbstractCell<Section> {

    @Override
    public void render(final Context context, final Section value, final SafeHtmlBuilder sb) {
      if (value != null) {
        sb.appendEscaped(value.getTitle());
      }
    }
  }

  private class SectionDataProvider extends AsyncDataProvider<Section> {

    private WebEntity continer;

    private SectionDataProvider(final WebEntity container) {
      this.continer = container;
    }

    @Override
    protected void onRangeChanged(final HasData<Section> display) {
      sectionService.find(continer).fire(new Receiver<List<Section>>() {
        @Override
        public void onSuccess(final List<Section> result) {
          display.setRowCount(result.size());
          display.setRowData(0, result);
        }
      });
    }
  }

  private class SectionTreeViewModel implements TreeViewModel {
    @Override
    public <T> NodeInfo<?> getNodeInfo(final T value) {
      if (value == null) {
        SectionDataProvider dataProvider = new SectionDataProvider(book);
        rootNode = new DefaultNodeInfo<Section>(dataProvider, getCell(), selectionModel, null);
        return rootNode;
      } else {
        Section section = (Section) value;
        SectionDataProvider dataProvider = new SectionDataProvider(section);
        return new DefaultNodeInfo<Section>(dataProvider, getCell(), selectionModel, null);
      }
    }

    @Override
    public boolean isLeaf(final Object value) {
      return false;
    }
  }

  DefaultNodeInfo<Section> rootNode;

  @Inject
  AsyncSectionService sectionService;

  Book book;

  private final Logger logger = Logger.getLogger(BookEditor.class.getName());

  private static Binder uiBinder = GWT.create(Binder.class);

  @UiField(provided = true)
  CellTree sectionsTree;

  @UiField
  SimplePanel editorPanel;

  @Inject
  SectionEditor selectionEditor;

  MultiSelectionModel<Section> selectionModel;

  public AbstractCell<Section> getCell() {
    return new DefaultCell();
  }

  @Override
  public Book getValue() {
    return book;
  }

  @Override
  public void setValue(final Book value) {
    this.book = value;
  }

  @Override
  protected void start() {

    selectionModel = new MultiSelectionModel<Section>();
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        Set<Section> selectedSet = selectionModel.getSelectedSet();
        if (selectedSet.size() == 1) {
          Section section = selectedSet.iterator().next();
          selectionEditor.setValue(section);
        }
      }
    });

    CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
    sectionsTree = new CellTree(new SectionTreeViewModel(), null, res);
    sectionsTree.setAnimationEnabled(true);
    // Create the UiBinder.
    Widget widget = uiBinder.createAndBindUi(this);

    editorPanel.setWidget(selectionEditor);
    main.add(widget);
  }

}
