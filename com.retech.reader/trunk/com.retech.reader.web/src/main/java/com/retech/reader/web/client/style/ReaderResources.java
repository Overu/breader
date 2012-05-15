package com.retech.reader.web.client.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

import java.util.logging.Logger;

public class ReaderResources {

  public interface Bundle extends ClientBundle {

    // @NotStrict
    // @Source("clean.css")
    // CssResource clean();

    // ImageResource retechcorpLogo();

    @Source("style.css")
    Style style();
  }

  public interface Style extends CssResource {
    String contentHtmlPanel();

    String contentSectionView();

    String contentTopBar();
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
