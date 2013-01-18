package com.goodow.web.reader.client;

import com.goodow.web.core.client.WebView;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.reader.client.style.ReadResources;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

@Singleton
public class BookDetail extends WebView<Widget> {

  interface Binder extends UiBinder<Widget, BookDetail> {
  }

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  ScrollPanel detail;

  @UiField(provided = true)
  Button buyButton;

  @UiField(provided = true)
  Image bookImage;

  @UiField
  TableCellElement authorElm;

  @Inject
  AsyncBookService bookService;

  @Override
  public void refresh() {
    if (place == null) {
      return;
    }
    bookService.getById(place.getParent().getPath()).fire(new Receiver<Book>() {
      @Override
      public void onSuccess(final Book result) {
        authorElm.setInnerHTML(result.getAuthor());
      }
    });
  }

  @Override
  protected Widget createRoot() {
    buyButton = new Button("buy");
    buyButton.setSmall(true);
    buyButton.setWidth("60px");
    buyButton.addTapHandler(new TapHandler() {

      @Override
      public void onTap(TapEvent event) {
        Window.alert("buy!");
      }
    });

    bookImage = new Image(ReadResources.INSTANCE().cover());
    return binder.createAndBindUi(this);
  }

  @Override
  protected void start() {
    StringBuffer buffer = new StringBuffer();

    detail.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss().fillPanelExpandChild());

    for (int i = 0; i < 10; i++) {
      buffer
          .append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur tincidunt, arcu eget accumsan ullamcorper, ante nisl viverra enim, id consequat ante metus sed nibh. Sed orci nisl, dictum sit amet bibendum id, mollis ac arcu. Mauris venenatis orci sed dui lacinia vulputate. Proin in commodo nisl. Curabitur libero sem, tincidunt et eleifend et, euismod ac arcu. Etiam sit amet nulla mauris, id pulvinar enim. Quisque tincidunt accumsan tempor. Donec et euismod augue. Quisque ultricies mollis metus cursus consectetur. Ut sollicitudin magna in velit vulputate tempus. Sed metus metus, tincidunt nec consectetur vitae, sagittis ac velit Vestibulum consectetur, velit sed consectetur tempor, sapien odio tempor nulla, vel interdum mauris mi ac sem. Proin fermentum dictum mattis. Praesent eleifend posuere orci, vel rhoncus ante consequat eu. Vivamus eu nisl ornare nibh pellentesque fringilla. Sed tincidunt felis gravida mauris gravida sed venenatis mauris tincidunt. Pellentesque varius neque non arcu dictum consequat. Nulla vitae orci felis, ac egestas nisl. Vivamus semper sollicitudin mollis. Donec ac diam ut magna tempor consectetur. Donec at metus ligula, sed hendrerit sapien. Proin quis urna dui, id tincidunt tellus. Morbi enim ligula, mollis ut congue non, commodo nec magna. Quisque sagittis vehicula dui, ac aliquam tortor scelerisque a. Morbi urna ipsum, feugiat vitae fringilla in, blandit adipiscing tellus. Donec in est id tortor viverra viverra. Proin eu arcu sem, eget tincidunt est. Nunc in erat risus. Praesent pharetra pulvinar volutpat. Donec semper diam in enim luctus in viverra nisi tincidunt. Aliquam cursus interdum posuere.");

    }
    HTML html = new HTML(buffer.toString());
    detail.setWidget(html);
  }
}
