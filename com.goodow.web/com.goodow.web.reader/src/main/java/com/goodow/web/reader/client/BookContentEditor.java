package com.goodow.web.reader.client;

import com.goodow.web.core.shared.Section;
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
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.TreeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BookContentEditor extends FormView<Book> {

  interface Binder extends UiBinder<Widget, BookContentEditor> {
  }

  private class DefaultCell extends AbstractCell<Section> {

    @Override
    public void render(final Context context, final Section value, final SafeHtmlBuilder sb) {
      if (value != null) {
        sb.appendEscaped(value.getTitle());
      }
    }
  }

  private class SectionDataProvider extends AsyncDataProvider<List<Section>> {

    @Override
    protected void onRangeChanged(final HasData<List<Section>> display) {

    }

  }

  private class SectionTreeViewModel implements TreeViewModel {
    @Override
    public <T> NodeInfo<?> getNodeInfo(final T value) {
      if (value == null && book != null) {
        return createNodeInfo(book.getSections());
      } else {
        Section item = (Section) value;
        if (item.getChildren() != null) {
          return createNodeInfo(item.getChildren());
        }
      }
      return null;
    }

    @Override
    public boolean isLeaf(final Object value) {
      return value != null && ((Section) value).getChildren() == null;
    }

    private DefaultNodeInfo<Section> createNodeInfo(final List<Section> sections) {
      ListDataProvider<Section> dataProvider = new ListDataProvider<Section>(sections);
      return new DefaultNodeInfo<Section>(dataProvider, getCell(), selectionModel, null);
    }
  }

  private SectionDataProvider dataProvider;

  Book book;
  private final Logger logger = Logger.getLogger(BookContentEditor.class.getName());

  private static Binder uiBinder = GWT.create(Binder.class);

  @UiField(provided = true)
  CellTree sectionEditor;

  @UiField
  SimplePanel resourceEditor;

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
    if (book.getSections() != null) {
    }
  }

  @Override
  protected void start() {
    dataProvider = new SectionDataProvider();
    selectionModel = new MultiSelectionModel<Section>();
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        List<Section> selected = new ArrayList<Section>(selectionModel.getSelectedSet());
      }
    });

    CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
    sectionEditor = new CellTree(new SectionTreeViewModel(), null, res);
    sectionEditor.setAnimationEnabled(true);

    // Create the UiBinder.
    Widget widget = uiBinder.createAndBindUi(this);
    main.add(widget);
  }

}
