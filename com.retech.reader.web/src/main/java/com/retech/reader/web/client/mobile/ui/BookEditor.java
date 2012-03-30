package com.retech.reader.web.client.mobile.ui;

import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widget.client.TextButton;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

import com.retech.reader.web.client.ui.widget.EditorDecorator;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.rpc.IssueContext;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.mvp.shared.rpc.BaseEditor;
import org.cloudlet.web.service.shared.rpc.BaseContext;

import java.util.Date;

@Singleton
public class BookEditor extends BaseEditor<IssueProxy> implements ValueChangeHandler {

  interface Binder extends UiBinder<Widget, BookEditor> {
  }

  /**
   * Editor driver for this view.
   */
  interface Driver extends RequestFactoryEditorDriver<IssueProxy, BookEditor> {
  }

  /**
   * The UiBinder used to generate the view.
   */
  private static Binder binder = GWT.create(Binder.class);
  private static Driver driver = GWT.create(Driver.class);

  @UiField
  @Ignore
  TextButton deleteButton;
  @UiField(provided = true)
  CellWidget<Date> createTimeEditor;
  EditorDecorator<String> titleEditor;

  @UiField
  @Ignore
  CellWidget<String> nameField;

  @UiField
  Element nameViolation;

  @UiField
  CellWidget<String> detailEditor;

  @UiField
  @Ignore
  TextButton playButton;

  private final ReaderFactory f;

  private final Provider<BasePlace> places;

  @Inject
  BookEditor(final ReaderFactory f, final Provider<BasePlace> places) {
    this.places = places;
    this.f = f;

    createTimeEditor = new CellWidget<Date>(new DatePickerCell());
    titleEditor = EditorDecorator.create(nameField.asEditor(), nameViolation);
  }

  @Override
  public void onStart(final ActivityState state) {
    nameField.addValueChangeHandler(this);
    detailEditor.addValueChangeHandler(this);
    createTimeEditor.addValueChangeHandler(this);

    super.onStart(state);
  }

  @Override
  public void onValueChange(final ValueChangeEvent event) {
    super.put(null);
  }

  @Override
  protected void edit(final IssueProxy value, final BaseContext context) {
    boolean isCreate = ((BasePlace) placeController.getWhere()).getProxyId() == null;
    deleteButton.setText(isCreate ? "取消" : "删除");
    playButton.setEnabled(!isCreate);
    super.edit(value, context);
  }

  @Override
  protected RequestFactoryEditorDriver<IssueProxy, BookEditor> provideEditorDriver() {
    return driver;
  }

  @Override
  protected IssueContext provideRequestContext() {
    return f.issue();
  }

  @Override
  protected UiBinder<?, ?> provideUiBinder() {
    return binder;
  }

  @UiFactory
  @Ignore
  CellWidget<String> cellWidgetProvider() {
    CellWidget<String> cellWidget = new CellWidget<String>(new EditTextCell());
    return cellWidget;
  }

  @UiHandler({"deleteButton", "playButton"})
  void onClickHandler(final ClickEvent event) {
    Object source = event.getSource();
    if (source == deleteButton) {
      super.remove(null);
    } else if (source == playButton) {
      BasePlace to = places.get().setPath(SectionListEditor.class.getName());
      // to.setParameter(IssueProxy.ID, f.getHistoryToken(state.getProxyId()));
      to.setParameter(proxy.stableId());
      placeController.goTo(to);
    }
  }
}
