package com.retech.reader.web.client.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Shared;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.ActivityAware;
import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.BasePlace;

@Singleton
public class BookListPanel extends Composite implements ActivityAware {

  interface Binder extends UiBinder<Widget, BookListPanel> {
  }

  interface Bundle extends ClientBundle {
    @Source("BookListPanel.portrait.css")
    Style portrait();
  }

  @Shared
  interface Style extends CssResource {
    String root();
  }

  private static Binder binder = GWT.create(Binder.class);
  private static Bundle bundle = GWT.create(Bundle.class);

  private ReaderFactory f;
  @UiField(provided = true)
  BookFlip bookFlipOfBookListPanel;
  @UiField(provided = true)
  LibraryView libraryView;

  private final Provider<BasePlace> places;

  @Inject
  BookListPanel(final ReaderFactory f, final Provider<BasePlace> places,
      final BookFlip bookFlipOfBookListPanel, final LibraryView libraryView) {
    this.f = f;
    this.places = places;
    this.bookFlipOfBookListPanel = bookFlipOfBookListPanel;
    this.libraryView = libraryView;
    initWidget(binder.createAndBindUi(this));
    String portraitCss =
        "@media only screen and (max-width: 1023px) {" + bundle.portrait().getText() + "}";
    StyleInjector.injectAtEnd(portraitCss);

  }

  @Override
  public void onStart(final ActivityState state) {
    bookFlipOfBookListPanel.onStart(state);
    libraryView.onStart(state);
  }
}
