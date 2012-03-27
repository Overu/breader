package com.goodow.web.ui.client.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;

import java.util.logging.Logger;

/**
 * The Styles used in webapp.
 */
public class WebAppResources {

  /**
   * Shared resources.
   */
  public interface Images extends ClientBundle {

    ImageResource loading();

    /**
     * Indicates the locale selection box.
     */
    ImageResource locale();

    @NotStrict
    @Source("WebApp.css")
    Styles styles();

    ImageResource webAppLogo();

  }

  /**
   * Common styles.
   */
  public interface Styles extends CssResource {
    String keyboardShortcuts();
  }

  private static Images images;
  private static Logger logger = Logger.getLogger(WebAppResources.class.getName());

  static {
    logger.finest("static init start");

    images = GWT.create(Images.class);
    images.styles().ensureInjected();

    logger.finest("static init end");
  }

  public static Images images() {
    return images;
  }

  public static Styles styles() {
    return images.styles();
  }
}
