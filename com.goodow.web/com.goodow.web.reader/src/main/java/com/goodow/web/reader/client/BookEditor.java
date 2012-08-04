package com.goodow.web.reader.client;

import com.goodow.web.core.shared.AsyncSectionService;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.Section;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;
import com.google.inject.Inject;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

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

  private class RootDataProvider extends AsyncDataProvider<Section> {
    @Override
    protected void onRangeChanged(final HasData<Section> display) {
      if (book == null) {
        display.setRowCount(0);
        display.setRowData(0, Collections.EMPTY_LIST);
        return;
      }
      sectionService.find(book).fire(new Receiver<List<Section>>() {
        @Override
        public void onSuccess(final List<Section> result) {
          book.setSections(result);
          display.setRowCount(result.size());
          display.setRowData(0, result);
        }
      });
    }
  }

  private class SectionDataProvider extends AsyncDataProvider<Section> {

    private Section parent;

    private SectionDataProvider(final Section container) {
      this.parent = container;
    }

    @Override
    protected void onRangeChanged(final HasData<Section> display) {
      sectionService.find(parent).fire(new Receiver<List<Section>>() {
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

  @Inject
  @UiField(provided = true)
  FlowPanel treeMenu;

  @Inject
  ActionButton addSection;

  @UiField(provided = true)
  CellTree sectionsTree;

  @UiField
  SimplePanel editorPanel;

  @Inject
  SectionEditor selectionEditor;

  MultiSelectionModel<Section> selectionModel;

  RootDataProvider rootProvider;

  @Inject
  AsyncBookService bookService;

  public AbstractCell<Section> getCell() {
    return new DefaultCell();
  }

  @Override
  public Book getValue() {
    return book;
  }

  public void redraw(final boolean reload) {
    if (rootProvider != null) {
      List<Section> sections = book.getSections();
      for (HasData<Section> display : rootProvider.getDataDisplays()) {
        if (reload || sections == null) {
          rootProvider.onRangeChanged(display);
        } else {
          display.setRowCount(sections.size());
          display.setRowData(0, sections);
        }
      }
    }
  }

  @Override
  public void refresh() {
    super.refresh();
    if (place != null) {
      bookService.getById(place.getPath()).fire(new Receiver<Book>() {
        @Override
        public void onSuccess(final Book result) {
          setValue(result);
        }
      });
    }
  }

  public void render() {
    for (HasData<Section> display : rootProvider.getDataDisplays()) {
      rootProvider.onRangeChanged(display);
    }
  }

  @Override
  public void setValue(final Book value) {
    this.book = value;
    redraw(false);
  }

  @Override
  protected void start() {
    rootProvider = new RootDataProvider();
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

    rootNode = new DefaultNodeInfo<Section>(rootProvider, getCell(), selectionModel, null);

    CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
    sectionsTree = new CellTree(new SectionTreeViewModel(), null, res);
    sectionsTree.setAnimationEnabled(true);
    // Create the UiBinder.
    Widget widget = uiBinder.createAndBindUi(this);

    editorPanel.setWidget(selectionEditor);
    main.add(widget);

    HeaderPanel header = new HeaderPanel();
    treeMenu.add(header);

    header.setCenterWidget(addSection);
    addSection.setText("添加");
    addSection.addTapHandler(new TapHandler() {

      @Override
      public void onTap(final TapEvent event) {
        int order = book.getSections().size() + 1;
        Section section = new Section();
        section.setTitle("Chapater " + order);
        section.setContainer(book);
        section.setDisplayOrder(order);
        sectionService.save(section).fire(new Receiver<Section>() {
          @Override
          public void onSuccess(final Section result) {
            book.getSections().add(result);
            redraw(false);
            selectionModel.setSelected(result, true);
          }
        });
      }
    });
  }

}
