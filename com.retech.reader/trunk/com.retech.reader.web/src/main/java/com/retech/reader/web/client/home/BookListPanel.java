package com.retech.reader.web.client.home;

import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.widget.toolbar.buttons.WaveToolBar;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Shared;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.mvp.shared.ProvideActivities;

import java.util.Arrays;
import java.util.List;

@Singleton
public class BookListPanel extends Composite implements ProvideActivities {

  interface Binder extends UiBinder<Widget, BookListPanel> {
  }

  interface Bundle extends ClientBundle {
    @Source("BookListPanel.portrait.css")
    Style portrait();
  }

  interface Resources extends ClientBundle {

    ImageResource refresh();

    ImageResource search();

    ImageResource settings();

    ImageResource share();
  }

  @Shared
  interface Style extends CssResource {
    String root();
  }

  // private static Resources res = GWT.create(Resources.class);
  private static Binder binder = GWT.create(Binder.class);
  private static Bundle bundle = GWT.create(Bundle.class);

  // @UiField(provided = true)
  // BookFlip bookFlipOfBookListPanel;
  @UiField(provided = true)
  MyDownLoadPanel myDownLoadPanel;

  // @UiField(provided = true)
  // LibraryView libraryView;

  // @UiField(provided = true)
  // WavePanel waveToolbar;

  // @UiField(provided = true)
  // SearchPanel searchPanel;

  @Inject
  BookListPanel(final Provider<BasePlace> places, final LibraryView libraryView,
      final WavePanel waveToolbar, final PlaceController placeController,
      final WaveToolBar toolbar, final SearchPanel searchPanel,
      final MyDownLoadPanel myDownLoadPanel) {
    // this.bookFlipOfBookListPanel = bookFlipOfBookListPanel;
    this.myDownLoadPanel = myDownLoadPanel;
    // this.libraryView = libraryView;
    // this.waveToolbar = waveToolbar;
    // this.searchPanel = searchPanel;
    // toolbar = waveToolbar.addWaveToolBar();
    initWidget(binder.createAndBindUi(this));

    // ToolBarClickButton settingButton = toolbar.addClickButton();
    // settingButton.setText("设置");
    // settingButton.setVisualElement(createIcon(res.settings()));
    //
    // ToolBarClickButton refreshButton = toolbar.addClickButton();
    // refreshButton.setText("刷新");
    // refreshButton.setVisualElement(createIcon(res.refresh()));
    //
    // ToolBarClickButton shareButton = toolbar.addClickButton();
    // shareButton.setText("分享");
    // shareButton.setVisualElement(createIcon(res.share()));
    //
    // ToolBarClickButton searchButton = toolbar.addClickButton();
    // searchButton.setText("搜索");
    // searchButton.setVisualElement(createIcon(res.search()));
    String portraitCss =
        "@media only screen and (max-width: 900px) {" + bundle.portrait().getText() + "}";
    StyleInjector.injectAtEnd(portraitCss);

    // settingButton.addClickHandler(new ClickHandler() {
    // @Override
    // public void onClick(final ClickEvent event) {
    // placeController.goTo(places.get().setPath(SettingsView.class.getName()));
    // }
    // });
    //
    // refreshButton.addClickHandler(new ClickHandler() {
    // @Override
    // public void onClick(final ClickEvent event) {
    // Window.Location.reload();
    // }
    // });
    //
    // shareButton.addClickHandler(new ClickHandler() {
    // @Override
    // public void onClick(final ClickEvent event) {
    // placeController.goTo(places.get().setPath(ContactPanel.class.getName()));
    // }
    // });
    //
    // searchButton.addClickHandler(new ClickHandler() {
    // @Override
    // public void onClick(final ClickEvent event) {
    // placeController.goTo(places.get().setPath(SearchPanel.class.getName()));
    // }
    // });

  }

  /**
   * Creates an icon Element.
   */
  public Element createIcon(final ImageResource imageResource) {
    return AbstractImagePrototype.create(imageResource).createElement();
  }

  @Override
  public List<Activity> provideActivities() {
    // return Arrays.<Activity> asList(bookFlipOfBookListPanel, libraryView);
    return Arrays.<Activity> asList(myDownLoadPanel);
  }

}
