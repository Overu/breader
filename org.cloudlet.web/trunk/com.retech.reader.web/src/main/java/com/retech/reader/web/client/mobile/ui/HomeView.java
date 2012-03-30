package com.retech.reader.web.client.mobile.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widget.client.TextButton;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.retech.reader.web.shared.proxy.IssueProxy;

import org.cloudlet.web.mvp.shared.ActivityAware;
import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.BasePlace;

import java.util.ArrayList;
import java.util.List;

public class HomeView extends Composite implements ActivityAware {
  interface Binder extends UiBinder<Widget, HomeView> {
  }

  private static Binder uiBinder = GWT.create(Binder.class);

  private final Provider<BookListEditor> provider;

  private final PlaceController placeController;

  private final Provider<BasePlace> simplePlace;
  private List<Widget> views = new ArrayList<Widget>();

  @UiField
  TextButton uploadBtn;

  @Inject
  public HomeView(final Provider<BookListEditor> provider, final PlaceController placeController,
      final Provider<BasePlace> simplePlace) {
    this.provider = provider;
    this.placeController = placeController;
    this.simplePlace = simplePlace;
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiHandler("uploadBtn")
  public void handleUpload(final ClickEvent event) {
    BasePlace place = simplePlace.get();
    place.setPath(IssueProxy.class.getName());
    placeController.goTo(place);
  }

  @Override
  public void onStart(final ActivityState state) {
    for (Widget w : views) {
      if (w instanceof ActivityAware) {
        ((ActivityAware) w).onStart(state);
      }
    }
  }

  @UiFactory
  BookListEditor createPanel() {
    BookListEditor listView = provider.get();
    views.add(listView);
    return listView;
  }
}
