package com.goodow.web.core.client;

import com.goodow.web.core.shared.Resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;

public class ImageField extends ResourceField {

  @Inject
  protected Image image;

  @Override
  public void setValue(final Resource value) {
    super.setValue(value);
    if (value != null) {
      image.setUrl(GWT.getModuleBaseURL() + "resources/" + resource.getId());
    }
  }

  @Override
  protected void start() {
    super.start();
    // fileUpload.removeFromParent();
    main.add(image);
  }
}
