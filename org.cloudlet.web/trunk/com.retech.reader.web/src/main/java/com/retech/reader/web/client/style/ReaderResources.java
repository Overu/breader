package com.retech.reader.web.client.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

import java.util.logging.Logger;

public class ReaderResources {

  public interface Bundle extends ClientBundle {

    ImageResource add();

    // @NotStrict
    // @Source("clean.css")
    // CssResource clean();

    ImageResource directory();

    ImageResource forward();

    ImageResource home();

    ImageResource refresh();

    ImageResource repeal();

    ImageResource search();

    ImageResource settings();

    ImageResource share();

    @Source("style.css")
    Style style();
  }

  public interface Style extends CssResource {
    String refresh();

    String search();

    String settings();

    String share();
  }

  private static Bundle BUNDLE;
  private static Logger logger = Logger.getLogger(ReaderResources.class.getName());

  static {
    logger.finest("static init start");
    BUNDLE = GWT.create(Bundle.class);
    // INSTANCE().clean().ensureInjected();
    INSTANCE().style().ensureInjected();
    logger.finest("static init end");
  }

  public static Bundle INSTANCE() {
    return BUNDLE;
  }
}
