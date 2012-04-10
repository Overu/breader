package com.retech.reader.web.client.home;

import com.goodow.web.view.wave.client.ToolbarClickButton;
import com.goodow.web.view.wave.client.WavePanel;
import com.goodow.web.view.wave.client.WaveToolbar;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

import com.retech.reader.web.client.mobile.ui.bar.SettingsView;
import com.retech.reader.web.client.mobile.ui.talk.TalkView;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.mvp.shared.ProvideActivities;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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

  private Logger logger = Logger.getLogger(getClass().getName());
  private static Resources res = GWT.create(Resources.class);
  private static Binder binder = GWT.create(Binder.class);
  private static Bundle bundle = GWT.create(Bundle.class);

  @UiField(provided = true)
  BookFlip bookFlipOfBookListPanel;
  @UiField(provided = true)
  LibraryView libraryView;
  @UiField(provided = true)
  WavePanel waveToolbar;

  @Inject
  BookListPanel(final Provider<BasePlace> places, final BookFlip bookFlipOfBookListPanel,
      final LibraryView libraryView, final WavePanel waveToolbar,
      final PlaceController placeController, WaveToolbar toolbar) {
    this.bookFlipOfBookListPanel = bookFlipOfBookListPanel;
    this.libraryView = libraryView;
    this.waveToolbar = waveToolbar;
    toolbar = waveToolbar.toolbar();
    initWidget(binder.createAndBindUi(this));

    ToolbarClickButton settingButton = toolbar.addClickButton();
    settingButton.setText("设置");
    settingButton.setVisualElement(createIcon(res.settings()));

    ToolbarClickButton refreshButton = toolbar.addClickButton();
    refreshButton.setText("刷新");
    refreshButton.setVisualElement(createIcon(res.refresh()));

    ToolbarClickButton shareButton = toolbar.addClickButton();
    shareButton.setText("分享");
    shareButton.setVisualElement(createIcon(res.share()));

    ToolbarClickButton searchButton = toolbar.addClickButton();
    searchButton.setText("搜索");
    searchButton.setVisualElement(createIcon(res.search()));
    String portraitCss =
        "@media only screen and (max-width: 900px) {" + bundle.portrait().getText() + "}";
    StyleInjector.injectAtEnd(portraitCss);

    settingButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        placeController.goTo(places.get().setPath(SettingsView.class.getName()));
      }
    });

    refreshButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        logger.info("开发中");
      }
    });

    shareButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        placeController.goTo(places.get().setPath(TalkView.class.getName()));
      }
    });

    searchButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        logger.info("开发中");
      }
    });

  }

  /**
   * Creates an icon Element.
   */
  public Element createIcon(final ImageResource imageResource) {
    return AbstractImagePrototype.create(imageResource).createElement();
  }

  @Override
  public List<Activity> provideActivities() {
    return Arrays.<Activity> asList(bookFlipOfBookListPanel, libraryView);
  }

}
